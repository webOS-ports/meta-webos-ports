SUMMARY = "Distribution specific configuration for Pulseaudio"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# /etc/default/pulseaudio.conf used to be installed here purely to set
# PULSE_SCRIPT=/etc/pulse/webos-system.pa, but nothing ever read that variable --
# pulseaudio.service's ExecStart names the script directly now, so the file had no
# remaining content or purpose.
SRC_URI = " \
    file://webos-system.pa \
"

do_install() {
    install -d ${D}${sysconfdir}/pulse
    install -m 0644 ${UNPACKDIR}/webos-system.pa ${D}${sysconfdir}/pulse/
}

FILES:${PN} = "${sysconfdir}/pulse"
