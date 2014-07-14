FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://change-before-dependencies.conf \
"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system/android-system.service.d
    install -m 0644 ${WORKDIR}/change-before-dependencies.conf \
        ${D}${systemd_unitdir}/system/android-system.service.d
}

FILES_${PN} += "${systemd_unitdir}/system/android-system.service.d"
