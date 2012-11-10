FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

SRCREV = "1614f9ae5bf237b192b1c39bafd8ede3377d96f9"
