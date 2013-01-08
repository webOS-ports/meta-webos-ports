FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 9}"

SRC_URI_append = " \
    file://enable-webosports-first-use.patch \
    file://dont-spawn-bootanimation-process.patch \
    file://use-webosports-wifi-settings-app.patch"

inherit webos-ports-submissions

SRCREV = "48382ab049407a6fb113bfae279f5e11d385aadb"

RDEPENDS_${PN} += "webappmanager keyboard-efigs"
