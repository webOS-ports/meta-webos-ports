FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://0001-Implement-stub-luneos-plugin-based-on-location-servi.patch;patch=1"

PACKAGECONFIG[luneos] = "OE_LUNEOS_ENABLED,,glib-2.0 luna-service2"
EXTRA_QMAKEVARS_PRE += "${@base_contains('PACKAGECONFIG', 'luneos', 'CONFIG+=luneos', '', d)}"

PACKAGECONFIG = "luneos"
