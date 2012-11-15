FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 3}"

SRC_URI += "file://connman.upstart"

do_install_append() {
    install -d ${D}${sysconfdir}/event.d
    install -m 0644 ${WORKDIR}/connman.upstart ${D}${sysconfdir}/event.d/connman
}

INITSCRIPT_PACKAGES = "${PN}-sysvinit"

PACKAGES =+ "${PN}-sysvinit ${PN}-upstart"
FILES_${PN}-sysvinit = "${sysconfdir}/init.d/connman"
FILES_${PN}-upstart = "${sysconfdir}/event.d/connman"
