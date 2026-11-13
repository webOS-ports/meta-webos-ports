/* SPDX-License-Identifier: Apache-2.0 */
/*
 * mtk-wifi-suspend - set WiFi driver suspend mode
 *
 * Sends "DRIVER SETSUSPENDMODE <0|1>" via SIOCDEVPRIVATE+1 with
 * auto-detected struct layout (MediaTek gen4m inline buf / Qualcomm
 * pointer buf), then falls back to NL80211 testmode CMD_ID 101 for
 * drivers that reject the ioctl.
 *
 * Gen2 (wlan_drv_gen2) accepts both the ioctl and testmode but filters
 * nothing in the firmware; this tool returns failure there immediately
 * so the caller can use the interface-down fallback instead.
 *
 * Usage: mtk-wifi-suspend <ifname> <0|1>
 * Returns 0 on success, 1 on failure (error on stderr).
 * No external library dependencies: pure libc + kernel sockets.
 */

#include <errno.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <net/if.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <linux/netlink.h>

/* SIOCDEVPRIVATE+1 */
#define PRIV_IOCTL  0x89F1u

/* MediaTek gen4m layout: buf inline, 512 bytes */
#define MTK_BUF  512
struct mtk_priv { char buf[MTK_BUF]; int used_len; int total_len; };

/* Qualcomm layout: pointer to buf */
struct qcom_priv { char *buf; int used_len; int total_len; int _pad; };

/*
 * Generic-netlink / NL80211 constants defined locally so this file
 * compiles without linux/genetlink.h or linux/nl80211.h.
 */
#define NETLINK_GENERIC           16
#define GENL_ID_CTRL              0x10u
#define CTRL_CMD_GETFAMILY        3
#define CTRL_ATTR_FAMILY_ID       1
#define CTRL_ATTR_FAMILY_NAME     2
#define GENL_HDRLEN               4   /* sizeof(struct genlmsghdr) */
#define NL80211_CMD_TESTMODE      45
#define NL80211_ATTR_IFINDEX      3
#define NL80211_ATTR_TESTDATA     69
#define TESTMODE_CMD_ID_SUSPEND   101

/* ------------------------------------------------------------------ */

static int is_mtk(void)
{
    static const char *paths[] = {
        "/dev/wmtWifi",
        "/sys/module/wlan_drv_gen4m",
        "/sys/module/wlan_drv_gen4m_6765",
        "/sys/module/wlan_drv_gen4m_6779",
        "/sys/module/wlan_drv_gen4m_6877",
        "/sys/module/wlan_drv_gen4m_6893",
        NULL,
    };
    const char **p;
    for (p = paths; *p; p++)
        if (access(*p, F_OK) == 0) return 1;
    return 0;
}

static int is_gen2(void)
{
    return access("/sys/module/wlan_drv_gen2", F_OK) == 0;
}

/* ------------------------------------------------------------------ */

static int try_ioctl(const char *ifname, int suspend)
{
    char cmd[32];
    struct ifreq ifr;
    int fd, ret;

    snprintf(cmd, sizeof(cmd), "SETSUSPENDMODE %d", suspend);
    fd = socket(AF_INET, SOCK_DGRAM, 0);
    if (fd < 0) return -errno;
    memset(&ifr, 0, sizeof(ifr));
    strncpy(ifr.ifr_name, ifname, IFNAMSIZ - 1);

    if (is_mtk()) {
        struct mtk_priv p;
        memset(&p, 0, sizeof(p));
        strncpy(p.buf, cmd, MTK_BUF - 1);
        p.used_len = (int)(strlen(cmd) + 1);
        p.total_len = MTK_BUF;
        ifr.ifr_data = (char *)&p;
        ret = ioctl(fd, PRIV_IOCTL, &ifr) < 0 ? -errno : 0;
    } else {
        char buf[4096];
        struct qcom_priv p;
        strncpy(buf, cmd, sizeof(buf) - 1);
        buf[sizeof(buf) - 1] = '\0';
        p.buf      = buf;
        p.used_len = (int)(strlen(cmd) + 1);
        p.total_len = (int)sizeof(buf);
        p._pad      = 0;
        ifr.ifr_data = (char *)&p;
        ret = ioctl(fd, PRIV_IOCTL, &ifr) < 0 ? -errno : 0;
    }

    close(fd);
    return ret;
}

/* ------------------------------------------------------------------ */

/* Minimal netlink write buffer */
struct nlbuf { char data[4096]; int len; };

static void nl_init(struct nlbuf *b, uint16_t type, uint16_t flags, uint32_t seq)
{
    struct nlmsghdr *h;
    b->len = NLMSG_HDRLEN + GENL_HDRLEN;
    memset(b->data, 0, b->len);
    h = (struct nlmsghdr *)b->data;
    h->nlmsg_type  = type;
    h->nlmsg_flags = flags;
    h->nlmsg_seq   = seq;
    b->data[NLMSG_HDRLEN + 1] = 1; /* genlmsghdr.version */
}

static void nl_set_cmd(struct nlbuf *b, uint8_t cmd)
{
    b->data[NLMSG_HDRLEN] = (char)cmd;
}

static void nl_attr(struct nlbuf *b, uint16_t type, const void *data, uint16_t dlen)
{
    int pad = (-(int)dlen) & 3;
    uint16_t nla_len = (uint16_t)(4 + dlen);
    char *p = b->data + b->len;
    memcpy(p,     &nla_len, 2);
    memcpy(p + 2, &type,    2);
    if (dlen) memcpy(p + 4, data, dlen);
    if (pad)  memset(p + 4 + dlen, 0, (size_t)pad);
    b->len += 4 + (int)dlen + pad;
}

static void nl_finish(struct nlbuf *b)
{
    ((struct nlmsghdr *)b->data)->nlmsg_len = (uint32_t)b->len;
}

static int try_testmode(const char *ifname, int suspend)
{
    struct sockaddr_nl sa;
    struct timeval tv;
    struct nlbuf req;
    char buf[4096];
    uint16_t nl80211 = 0;
    unsigned int ifidx;
    int fd, ret = -EOPNOTSUPP;

    fd = socket(AF_NETLINK, SOCK_RAW, NETLINK_GENERIC);
    if (fd < 0) return -errno;

    memset(&sa, 0, sizeof(sa));
    sa.nl_family = AF_NETLINK;
    if (bind(fd, (struct sockaddr *)&sa, sizeof(sa)) < 0) {
        ret = -errno; goto out;
    }
    memset(&tv, 0, sizeof(tv));
    tv.tv_sec = 3;
    setsockopt(fd, SOL_SOCKET, SO_RCVTIMEO, &tv, sizeof(tv));

    /* -- resolve nl80211 family ID ----------------------------------- */
    nl_init(&req, GENL_ID_CTRL, NLM_F_REQUEST | NLM_F_ACK, 1);
    nl_set_cmd(&req, CTRL_CMD_GETFAMILY);
    nl_attr(&req, CTRL_ATTR_FAMILY_NAME, "nl80211\0", 8);
    nl_finish(&req);
    if (send(fd, req.data, (size_t)req.len, 0) < 0) { ret = -errno; goto out; }

    while (!nl80211) {
        struct nlmsghdr *h;
        ssize_t n = recv(fd, buf, sizeof(buf), 0);
        char *a; int alen;
        if (n < 0) { ret = -errno; goto out; }
        h = (struct nlmsghdr *)buf;
        if ((size_t)n < NLMSG_HDRLEN || h->nlmsg_seq != 1) continue;
        if (h->nlmsg_type == NLMSG_ERROR) {
            int err; memcpy(&err, buf + NLMSG_HDRLEN, 4);
            if (err == 0) continue;
            ret = err < 0 ? err : -err; goto out;
        }
        a    = buf + NLMSG_HDRLEN + GENL_HDRLEN;
        alen = (int)(n  - NLMSG_HDRLEN - GENL_HDRLEN);
        while (alen >= 4) {
            uint16_t ln, tp; int skip;
            memcpy(&ln, a, 2); memcpy(&tp, a + 2, 2);
            if (ln < 4 || ln > (uint16_t)alen) break;
            if (tp == CTRL_ATTR_FAMILY_ID && ln >= 6) {
                memcpy(&nl80211, a + 4, 2); break;
            }
            skip = (ln + 3) & ~3;
            a += skip; alen -= skip;
        }
    }
    if (!nl80211) { ret = -ENOENT; goto out; }

    /* -- send testmode suspend command ------------------------------- */
    ifidx = if_nametoindex(ifname);
    if (!ifidx) { ret = -ENODEV; goto out; }

    {
        /* struct testmode_cmd_suspend {u32 idx; u32 buflen; u8 suspend; u8 pad[3]} */
        struct { uint32_t idx; uint32_t buflen; uint8_t sus; uint8_t pad[3]; } tmcmd;
        uint32_t idx32 = ifidx;
        memset(&tmcmd, 0, sizeof(tmcmd));
        tmcmd.idx = TESTMODE_CMD_ID_SUSPEND;
        tmcmd.sus = (uint8_t)suspend;

        nl_init(&req, nl80211, NLM_F_REQUEST | NLM_F_ACK, 2);
        nl_set_cmd(&req, NL80211_CMD_TESTMODE);
        nl_attr(&req, NL80211_ATTR_IFINDEX, &idx32,  sizeof(idx32));
        nl_attr(&req, NL80211_ATTR_TESTDATA, &tmcmd, sizeof(tmcmd));
        nl_finish(&req);
    }
    if (send(fd, req.data, (size_t)req.len, 0) < 0) { ret = -errno; goto out; }

    while (1) {
        struct nlmsghdr *h;
        ssize_t n = recv(fd, buf, sizeof(buf), 0);
        int err;
        if (n < 0) { ret = -errno; goto out; }
        h = (struct nlmsghdr *)buf;
        if ((size_t)n < NLMSG_HDRLEN || h->nlmsg_seq != 2) continue;
        if (h->nlmsg_type == NLMSG_ERROR) {
            memcpy(&err, buf + NLMSG_HDRLEN, 4);
            ret = (err == 0) ? 0 : (err < 0 ? err : -err);
            goto out;
        }
        if (h->nlmsg_type == NLMSG_DONE) { ret = 0; goto out; }
    }
out:
    close(fd);
    return ret;
}

/* ------------------------------------------------------------------ */

int main(int argc, char **argv)
{
    const char *ifname;
    int suspend, r;

    if (argc != 3) {
        fprintf(stderr, "usage: mtk-wifi-suspend <ifname> <0|1>\n");
        return 1;
    }
    ifname  = argv[1];
    suspend = atoi(argv[2]);
    if (suspend != 0 && suspend != 1) {
        fprintf(stderr, "mtk-wifi-suspend: suspend must be 0 or 1\n");
        return 1;
    }

    /*
     * gen2 (wlan_drv_gen2) accepts SETSUSPENDMODE and testmode but filters
     * nothing: measured 2026-09-22, 0% asleep with testmode vs 98.5% with
     * interface down.  Fail immediately so the caller uses iface_down.
     */
    if (is_gen2()) {
        fprintf(stderr, "mtk-wifi-suspend: gen2 accepts commands but does not filter\n");
        return 1;
    }

    r = try_ioctl(ifname, suspend);
    if (r == 0) return 0;
    if (r != -EINVAL && r != -EOPNOTSUPP)
        fprintf(stderr, "mtk-wifi-suspend: SETSUSPENDMODE %d: %s\n",
                suspend, strerror(-r));

    r = try_testmode(ifname, suspend);
    if (r == 0) return 0;
    fprintf(stderr, "mtk-wifi-suspend: testmode CMD_ID=%d suspend=%d: %s\n",
            TESTMODE_CMD_ID_SUSPEND, suspend, strerror(-r));
    return 1;
}
