DESCRIPTION = "Machine specific configuration files for PulseAudio"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
PR = "r0"

PACKAGE_ARCH = "${MACHINE_ARCH}"
CONFLICTS = "pulseaudio-conf"

SRC_URI_append_tuna = " \
  file://system.pa \
  file://daemon.conf"

do_install() {
    if [ "${MACHINE}" = "tuna" ]; then
        install -d ${D}${sysconfdir}/pulse
        install -m 0644 ${WORKDIR}/system.pa ${D}${sysconfdir}/pulse/
        install -m 0644 ${WORKDIR}/daemon.conf ${D}${sysconfdir}/pulse/
    fi
}

PACKAGES = "${PN}"
ALLOW_EMPTY_${PN} = "1"
FILES_${PN} = "${sysconfdir}/pulse/"
