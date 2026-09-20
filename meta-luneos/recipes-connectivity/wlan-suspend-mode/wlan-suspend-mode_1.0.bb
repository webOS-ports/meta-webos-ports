SUMMARY = "Put the Qualcomm or MediaTek Halium WLAN driver into screen-off (SETSUSPENDMODE) mode while the display is off"
DESCRIPTION = "prima (WCNSS) arms its firmware broadcast/multicast filter, ARP/NS offload and \
multicast list only through Android's private DRIVER SETSUSPENDMODE ioctl, not from the cfg80211 \
suspend callback. Without it every LAN broadcast wakes the suspended phone (qcom_rx_wakelock). \
This service follows luna://com.palm.display/control/status and sends the command the way \
Android's framework does on screen off/on. No-op on qcacld-3.0."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
    file://wlan-suspend-mode \
    file://wlan-suspend-mode.service \
"

# file:// sources land directly in UNPACKDIR; there is no ${BP} subdirectory.
S = "${UNPACKDIR}"

inherit systemd

PR = "r3"

RDEPENDS:${PN} = "python3-core python3-ctypes python3-fcntl luna-service2"
COMPATIBLE_MACHINE = "(tissot|sargo|halium)"

do_install() {
    install -d ${D}${sbindir} ${D}${systemd_system_unitdir}
    install -m 0755 ${UNPACKDIR}/wlan-suspend-mode ${D}${sbindir}/wlan-suspend-mode
    install -m 0644 ${UNPACKDIR}/wlan-suspend-mode.service ${D}${systemd_system_unitdir}/
}

SYSTEMD_SERVICE:${PN} = "wlan-suspend-mode.service"
SYSTEMD_AUTO_ENABLE = "enable"
FILES:${PN} += "${systemd_system_unitdir}/wlan-suspend-mode.service"
