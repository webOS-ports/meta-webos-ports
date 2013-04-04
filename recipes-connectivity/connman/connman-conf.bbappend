
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://wired-setup "

do_install_append() {
    # We have only wired-setup, remove test for wired.confg
    if test -e ${WORKDIR}/wired-setup; then
        install -d ${D}${libdir}/connman
        install -m 0755 ${WORKDIR}/wired-setup ${D}${libdir}/connman
    fi
}
