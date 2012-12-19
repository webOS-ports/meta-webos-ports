FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 9}"

SRC_URI_append = " \
    file://enable-webosports-first-use.patch \
    file://dont-spawn-bootanimation-process.patch \
    file://use-webosports-wifi-settings-app.patch"

inherit webos-ports-submissions

SRCREV = "c269d9bd7f4cf498f030348f49992c80d5b50131"

RDEPENDS_${PN} += "webappmanager keyboard-efigs"
