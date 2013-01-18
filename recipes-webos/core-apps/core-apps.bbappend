FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 5}"

inherit webos-ports-submissions

SRCREV = "e4c97d90d05acab78eb7cff2299943a1556f5b94"

do_install_append() {
    # Drop the notes application, we ship our own
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.notes
    rm -rf ${D}${sysconfdir}/palm/db/permissions/com.palm.note
    rm -rf ${D}${sysconfdir}/palm/db/kinds/com.palm.note
}
