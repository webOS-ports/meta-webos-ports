
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://wired-setup \
    file://environment.conf \
    file://main.conf \
"

do_install_append() {
    # We have only wired-setup, remove test for wired.confg
    if test -e ${WORKDIR}/wired-setup; then
        install -d ${D}${datadir}/connman
        install -m 0755 ${WORKDIR}/wired-setup ${D}${datadir}/connman
    fi

    install -d ${D}${sysconfdir}/connman
    install -m 0644 ${WORKDIR}/environment.conf ${D}${sysconfdir}/connman/
    install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/
}

FILES_${PN} += "${sysconfdir}/connman"
