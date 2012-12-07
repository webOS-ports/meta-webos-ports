FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 5}"

inherit webos-ports-submissions

SRCREV = "2036ffe16ce89190165f12d169d729ad3df3ab41"

do_install_append() {
    # Drop the notes application, we ship our own
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.notes
    rm -rf ${D}${sysconfdir}/palm/db/permissions/com.palm.note
    rm -rf ${D}${sysconfdir}/palm/db/kinds/com.palm.note
}
