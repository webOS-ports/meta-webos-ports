SUMMARY = "Make the cfg80211 regulatory database take effect after boot"
DESCRIPTION = "cfg80211 is built into the kernel on the devices in this tree, so it requests \
regulatory.db during the kernel's initcalls, before any root filesystem exists. The load fails, \
the sysfs firmware fallback is answered \"not found\" by the Android container's ueventd, and \
net/wireless/reg.c latches the failure in a static error pointer that no later request_firmware \
can clear. The result is a radio permanently in the world regulatory domain, where every 5 GHz \
band is NO-IR/PASSIVE-SCAN, and an \"iw reg set\" that succeeds and does nothing. This service \
issues NL80211_CMD_RELOAD_REGDB once at startup, which is the only interface that clears the \
latch, and orders itself before wpa_supplicant and connman so the first scan already has a real \
regulatory domain."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
    file://cfg80211-regdb-reload \
    file://cfg80211-regdb-reload.service \
"

# file:// sources land directly in UNPACKDIR; there is no ${BP} subdirectory.
S = "${UNPACKDIR}"

inherit allarch systemd

PR = "r0"

# iw for the nl80211 command; wireless-regdb-static for the file itself. Both
# are hard requirements - without either, the unit's own conditions make it a
# no-op, which would hide the problem rather than fix it.
RDEPENDS:${PN} = "iw wireless-regdb-static"

# allarch, and deliberately not gated on MACHINE. A shell script and a unit
# file have nothing machine-specific in them, every machine with a cfg80211
# driver has this problem, and several of them (athena among them) flash the
# generic halium-arm64 image rather than a per-machine rootfs, so anything
# gated on MACHINE would simply not be there. The unit gates itself at runtime
# on /lib/firmware/regulatory.db instead.

do_install() {
    install -d ${D}${sbindir} ${D}${systemd_system_unitdir}
    install -m 0755 ${UNPACKDIR}/cfg80211-regdb-reload ${D}${sbindir}/cfg80211-regdb-reload
    install -m 0644 ${UNPACKDIR}/cfg80211-regdb-reload.service ${D}${systemd_system_unitdir}/
}

SYSTEMD_SERVICE:${PN} = "cfg80211-regdb-reload.service"
SYSTEMD_AUTO_ENABLE = "enable"
FILES:${PN} += "${systemd_system_unitdir}/cfg80211-regdb-reload.service"
