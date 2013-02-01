FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 5}"

inherit webos-ports-submissions

SRCREV = "8ac6fe5262ad7dc22ddc6678e1b7fee4c78f08b5"

do_install_append() {
    # Drop the notes application, we ship our own
    rm -rf ${D}${webos_applicationsdir}/com.palm.app.notes
    rm -rf ${D}${sysconfdir}/palm/db/permissions/com.palm.note
    rm -rf ${D}${sysconfdir}/palm/db/kinds/com.palm.note
}
