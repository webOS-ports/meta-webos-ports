require mojomail.inc

SUMMARY = "IMAP sync transport for Open webOS"

DEPENDS += "mojomail-common c-ares"

PV = "2.0.0-99+git${SRCPV}"
SRCREV = "25880e13ba1fe62497c27ea1f3174e6e0ff17492"

inherit webos_filesystem_paths
inherit webos_system_bus

S = "${WORKDIR}/git/imap"

FILES_${PN} += "${webos_accttemplatesdir}"
