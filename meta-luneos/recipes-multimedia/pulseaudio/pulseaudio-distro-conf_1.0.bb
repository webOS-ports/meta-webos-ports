SUMMARY = "Distribution specific configuration for Pulseaudio"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Nothing but local files, so nothing ever lands in the default
# S = "${UNPACKDIR}/${BP}" and do_unpack warns about it.
S = "${UNPACKDIR}"

# /etc/default/pulseaudio.conf used to be installed here purely to set
# PULSE_SCRIPT=/etc/pulse/webos-system.pa, but nothing ever read that variable --
# pulseaudio.service's ExecStart names the script directly now, so the file had no
# remaining content or purpose.
# webos-virtual-devices.pa is deliberately not per-machine: the names it declares
# have to match module-palm-policy's virtualsinkmap[]/virtualsourcemap[], which is
# the same everywhere, and a missing one aborts pulseaudio rather than degrading.
# Machine-specific webos-system.pa files include it.
SRC_URI = " \
    file://webos-system.pa \
    file://webos-virtual-devices.pa \
"

do_install() {
    install -d ${D}${sysconfdir}/pulse
    install -m 0644 ${UNPACKDIR}/webos-system.pa ${D}${sysconfdir}/pulse/
    install -m 0644 ${UNPACKDIR}/webos-virtual-devices.pa ${D}${sysconfdir}/pulse/
}

FILES:${PN} = "${sysconfdir}/pulse"
