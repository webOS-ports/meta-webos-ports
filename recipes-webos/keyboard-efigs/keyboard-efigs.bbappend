FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 0}"

inherit webos-ports-submissions

SRCREV = "d24465d5966bebc55d636c7bbc1b76c55bdd6598"
