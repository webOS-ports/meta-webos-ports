SUMMARY = "Per-press report of power-key wake and display-on latency"
DESCRIPTION = "For every power-key press this boot, prints how long the display \
took to come on, how long before the press the last resume from suspend was, and \
what the kernel gave as the wake cause - which is what separates a slow wake \
(the SoC was suspended) from a slow display (it was not). The companion to \
luneos-power-report: that one explains battery drain, this one explains the \
delay between pressing the key and seeing something."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"
SECTION = "base"

SRC_URI = "file://luneos-wake-report"

# file:// sources land directly in UNPACKDIR; there is no ${BP} subdirectory.
S = "${UNPACKDIR}"

# journalctl is systemd's; grep and awk are busybox. Everything it reads is
# probed for and skipped when a platform does not log it, so nothing else is
# required.
RDEPENDS:${PN} = "systemd"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/luneos-wake-report ${D}${bindir}
}
