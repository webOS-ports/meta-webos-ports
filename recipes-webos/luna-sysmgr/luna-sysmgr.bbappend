FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 9}"

SRC_URI_append = " \
    file://enable-webosports-first-use.patch \
    file://dont-spawn-bootanimation-process.patch \
    file://use-webosports-wifi-settings-app.patch"

inherit webos-ports-submissions

SRCREV = "c6fe8df1e52d47b4cc0c6945fbd5c02d251745a2"

RDEPENDS_${PN} += "webappmanager keyboard-efigs"
