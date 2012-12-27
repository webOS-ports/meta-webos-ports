FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 9}"

SRC_URI_append = " \
    file://enable-webosports-first-use.patch \
    file://dont-spawn-bootanimation-process.patch \
    file://use-webosports-wifi-settings-app.patch"

inherit webos-ports-submissions

SRCREV = "4f29b5b9335a7944c5470b5eaba8c4b72f94eb14"
