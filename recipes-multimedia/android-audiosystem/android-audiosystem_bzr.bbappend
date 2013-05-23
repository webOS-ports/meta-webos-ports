FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://asound.conf"

do_install_append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/asound.conf ${D}${sysconfdir}
}

FILES_${PN} += "${sysconfdir}/asound.conf"
CONFFILES_${PN} += "${sysconfdir}/asound.conf"
