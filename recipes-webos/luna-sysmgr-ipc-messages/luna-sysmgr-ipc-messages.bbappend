FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

SRC_URI += "file://0001-Rework-ViewHost_PrepareAddWindow-ViewHost_PrepareAdd.patch"
