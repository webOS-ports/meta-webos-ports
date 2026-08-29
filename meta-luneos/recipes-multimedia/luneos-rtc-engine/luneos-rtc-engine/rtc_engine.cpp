// Copyright (c) 2026 LuneOS project
// SPDX-License-Identifier: Apache-2.0
//
// luneos-rtc-engine: real-time video media engine for calls on Halium
// devices. The network side of a call (e.g. meowcaller's WhatsApp
// Call.SendVideo/ReceiveVideo) exchanges H.264 Annex-B access units with
// this engine over SOCK_SEQPACKET unix sockets - one access unit per
// datagram, boundaries preserved by the socket type.
//
//   TX: droidcamsrc (recorder mode, hardware H264, viewfinder-independent)
//       -> h264parse (AU aligned) -> appsink -> tx socket
//   RX: rx socket -> appsrc -> h264parse -> decodebin (droiddec on halium)
//       -> fakesink (v1; a wayland-foreign sink follows for on-screen)
//
// v1 control is argv; the LS2/uMS pipeline wrapper builds on top.

#include <gst/gst.h>
#include <glib-unix.h>

#include <cstdio>
#include <cstring>
#include <string>

#include <sys/socket.h>
#include <sys/un.h>
#include <unistd.h>

struct Options
{
    std::string txSocket;
    std::string rxSocket;
    int camera  = 0;
    int width   = 1280;
    int height  = 720;
    int bitrate = 2000000;
    int fps     = 30;
};

static GMainLoop *loop;
static guint64 txAUs, rxAUs, rxFrames;

/* ---------------- TX ---------------- */

struct TxState
{
    GstElement *pipeline = nullptr;
    int listenFd         = -1;
    int peerFd           = -1;
} tx;

static gboolean txAcceptCb(gint fd, GIOCondition, gpointer)
{
    int peer = accept(fd, nullptr, nullptr);
    if (peer >= 0)
    {
        if (tx.peerFd >= 0)
            close(tx.peerFd);
        tx.peerFd = peer;
        int sz    = 1 << 20;
        setsockopt(peer, SOL_SOCKET, SO_SNDBUF, &sz, sizeof(sz));
        g_print("tx: peer connected\n");
    }
    return G_SOURCE_CONTINUE;
}

static GstFlowReturn txSampleCb(GstElement *sink, gpointer)
{
    GstSample *sample = nullptr;
    g_signal_emit_by_name(sink, "pull-sample", &sample);
    if (!sample)
        return GST_FLOW_OK;

    GstBuffer *buf = gst_sample_get_buffer(sample);
    GstMapInfo map;
    if (buf && gst_buffer_map(buf, &map, GST_MAP_READ))
    {
        if (tx.peerFd >= 0)
        {
            if (send(tx.peerFd, map.data, map.size, MSG_NOSIGNAL) < 0)
            {
                g_printerr("tx: peer gone (%s)\n", strerror(errno));
                close(tx.peerFd);
                tx.peerFd = -1;
            }
            else
            {
                txAUs++;
            }
        }
        gst_buffer_unmap(buf, &map);
    }
    gst_sample_unref(sample);
    return GST_FLOW_OK;
}

static bool startTx(const Options &o)
{
    unlink(o.txSocket.c_str());
    tx.listenFd = socket(AF_UNIX, SOCK_SEQPACKET | SOCK_CLOEXEC, 0);
    sockaddr_un addr{};
    addr.sun_family = AF_UNIX;
    g_strlcpy(addr.sun_path, o.txSocket.c_str(), sizeof(addr.sun_path));
    if (bind(tx.listenFd, (sockaddr *)&addr, sizeof(addr)) < 0 ||
        listen(tx.listenFd, 1) < 0)
    {
        g_printerr("tx: bind/listen %s: %s\n", o.txSocket.c_str(), strerror(errno));
        return false;
    }
    g_unix_fd_add(tx.listenFd, G_IO_IN, txAcceptCb, nullptr);

    gchar *desc = g_strdup_printf(
        "droidcamsrc name=cam camera-device=%d target-bitrate=%d "
        "cam.imgsrc ! fakesink async=false "
        "cam.vfsrc ! capsfilter caps=video/x-raw,format=NV21 ! "
        "queue leaky=downstream max-size-buffers=4 ! fakesink sync=false "
        "cam.vidsrc ! video/x-h264,width=%d,height=%d ! "
        "h264parse config-interval=-1 ! "
        "video/x-h264,stream-format=byte-stream,alignment=au ! "
        "appsink name=txsink emit-signals=true sync=false max-buffers=8 drop=false",
        o.camera, o.bitrate, o.width, o.height);
    GError *err  = nullptr;
    tx.pipeline  = gst_parse_launch(desc, &err);
    g_free(desc);
    if (!tx.pipeline)
    {
        g_printerr("tx pipeline: %s\n", err ? err->message : "?");
        g_clear_error(&err);
        return false;
    }

    GstElement *sink = gst_bin_get_by_name(GST_BIN(tx.pipeline), "txsink");
    g_signal_connect(sink, "new-sample", G_CALLBACK(txSampleCb), nullptr);
    gst_object_unref(sink);

    gst_element_set_state(tx.pipeline, GST_STATE_PLAYING);

    /* video mode + start-capture begins the hardware-encoded stream on
     * vidsrc while the raw viewfinder branch keeps the preview path
     * available (needs gst-droid's recorder-in-raw-preview patch). The
     * camera takes a few seconds to open; switching modes or starting the
     * capture before the device is running is silently ignored. */
    g_timeout_add_seconds(4, +[](gpointer) -> gboolean {
        GstElement *cam = gst_bin_get_by_name(GST_BIN(tx.pipeline), "cam");
        g_object_set(cam, "mode", 2, nullptr);
        gst_object_unref(cam);
        g_timeout_add_seconds(1, +[](gpointer) -> gboolean {
            GstElement *cam2 = gst_bin_get_by_name(GST_BIN(tx.pipeline), "cam");
            g_signal_emit_by_name(cam2, "start-capture");
            gst_object_unref(cam2);
            g_print("tx: capture started\n");
            return G_SOURCE_REMOVE;
        }, nullptr);
        return G_SOURCE_REMOVE;
    }, nullptr);

    g_print("tx: streaming camera %d h264 %dx%d @%dbps to %s\n", o.camera,
            o.width, o.height, o.bitrate, o.txSocket.c_str());
    return true;
}

/* ---------------- RX ---------------- */

struct RxState
{
    GstElement *pipeline = nullptr;
    GstElement *appsrc   = nullptr;
    int listenFd         = -1;
    int peerFd           = -1;
    guint peerWatch      = 0;
} rx;

static gboolean rxDataCb(gint fd, GIOCondition cond, gpointer)
{
    static char buf[1 << 20];
    ssize_t n = recv(fd, buf, sizeof(buf), 0);
    if (n <= 0)
    {
        g_print("rx: peer disconnected\n");
        close(fd);
        rx.peerFd = -1;
        return G_SOURCE_REMOVE;
    }

    GstBuffer *gbuf = gst_buffer_new_allocate(nullptr, n, nullptr);
    gst_buffer_fill(gbuf, 0, buf, n);
    GstFlowReturn fret;
    g_signal_emit_by_name(rx.appsrc, "push-buffer", gbuf, &fret);
    gst_buffer_unref(gbuf);
    rxAUs++;
    return G_SOURCE_CONTINUE;
}

static gboolean rxAcceptCb(gint fd, GIOCondition, gpointer)
{
    int peer = accept(fd, nullptr, nullptr);
    if (peer >= 0)
    {
        if (rx.peerFd >= 0)
            close(rx.peerFd);
        rx.peerFd = peer;
        g_unix_fd_add(peer, G_IO_IN, rxDataCb, nullptr);
        g_print("rx: peer connected\n");
    }
    return G_SOURCE_CONTINUE;
}

static GstPadProbeReturn rxFrameProbe(GstPad *, GstPadProbeInfo *info, gpointer)
{
    if (GST_PAD_PROBE_INFO_TYPE(info) & GST_PAD_PROBE_TYPE_BUFFER)
        rxFrames++;
    return GST_PAD_PROBE_OK;
}

static bool startRx(const Options &o)
{
    unlink(o.rxSocket.c_str());
    rx.listenFd = socket(AF_UNIX, SOCK_SEQPACKET | SOCK_CLOEXEC, 0);
    sockaddr_un addr{};
    addr.sun_family = AF_UNIX;
    g_strlcpy(addr.sun_path, o.rxSocket.c_str(), sizeof(addr.sun_path));
    if (bind(rx.listenFd, (sockaddr *)&addr, sizeof(addr)) < 0 ||
        listen(rx.listenFd, 1) < 0)
    {
        g_printerr("rx: bind/listen %s: %s\n", o.rxSocket.c_str(), strerror(errno));
        return false;
    }
    g_unix_fd_add(rx.listenFd, G_IO_IN, rxAcceptCb, nullptr);

    GError *err = nullptr;
    rx.pipeline = gst_parse_launch(
        "appsrc name=rxsrc is-live=true format=time do-timestamp=true "
        "caps=video/x-h264,stream-format=byte-stream,alignment=au ! "
        "h264parse ! droidvdec ! fakesink name=rxsink sync=false",
        &err);
    if (!rx.pipeline)
    {
        g_printerr("rx pipeline: %s\n", err ? err->message : "?");
        g_clear_error(&err);
        return false;
    }

    rx.appsrc = gst_bin_get_by_name(GST_BIN(rx.pipeline), "rxsrc");

    GstElement *sink = gst_bin_get_by_name(GST_BIN(rx.pipeline), "rxsink");
    GstPad *pad      = gst_element_get_static_pad(sink, "sink");
    gst_pad_add_probe(pad, GST_PAD_PROBE_TYPE_BUFFER, rxFrameProbe, nullptr,
                      nullptr);
    gst_object_unref(pad);
    gst_object_unref(sink);

    gst_element_set_state(rx.pipeline, GST_STATE_PLAYING);
    g_print("rx: decoding from %s\n", o.rxSocket.c_str());
    return true;
}

/* ---------------- main ---------------- */

static gboolean statsCb(gpointer)
{
    g_print("stats: tx_aus=%" G_GUINT64_FORMAT " rx_aus=%" G_GUINT64_FORMAT
            " rx_frames=%" G_GUINT64_FORMAT "\n",
            txAUs, rxAUs, rxFrames);
    return G_SOURCE_CONTINUE;
}

static gboolean quitCb(gpointer)
{
    g_main_loop_quit(loop);
    return G_SOURCE_REMOVE;
}

int main(int argc, char **argv)
{
    gst_init(&argc, &argv);

    Options o;
    for (int i = 1; i < argc; i++)
    {
        std::string a = argv[i];
        auto val      = [&a](const char *k) -> const char * {
            size_t l = strlen(k);
            return a.compare(0, l, k) == 0 ? a.c_str() + l : nullptr;
        };
        if (const char *v = val("--tx="))
            o.txSocket = v;
        else if (const char *v = val("--rx="))
            o.rxSocket = v;
        else if (const char *v = val("--camera="))
            o.camera = atoi(v);
        else if (const char *v = val("--bitrate="))
            o.bitrate = atoi(v);
    }

    if (o.txSocket.empty() && o.rxSocket.empty())
    {
        g_printerr("usage: %s [--tx=SOCK] [--rx=SOCK] [--camera=N] [--bitrate=BPS]\n",
                   argv[0]);
        return 1;
    }

    loop = g_main_loop_new(nullptr, FALSE);

    if (!o.txSocket.empty() && !startTx(o))
        return 1;
    if (!o.rxSocket.empty() && !startRx(o))
        return 1;

    g_timeout_add_seconds(2, statsCb, nullptr);
    g_unix_signal_add(SIGINT, quitCb, nullptr);
    g_unix_signal_add(SIGTERM, quitCb, nullptr);

    g_main_loop_run(loop);

    if (tx.pipeline)
    {
        GstElement *cam = gst_bin_get_by_name(GST_BIN(tx.pipeline), "cam");
        if (cam)
        {
            g_signal_emit_by_name(cam, "stop-capture");
            gst_object_unref(cam);
        }
        gst_element_set_state(tx.pipeline, GST_STATE_NULL);
    }
    if (rx.pipeline)
        gst_element_set_state(rx.pipeline, GST_STATE_NULL);
    return 0;
}
