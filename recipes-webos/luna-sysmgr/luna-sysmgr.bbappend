FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 4}"

inherit webos-ports-submissions

SRCREV = "ca9b8fa6d6cb943a1fd5fc9af2e75fb855d5745f"
