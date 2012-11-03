FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

SRCREV = "7a77fc9df4ca57bf817fbdeecac9f62599c7e03e"
