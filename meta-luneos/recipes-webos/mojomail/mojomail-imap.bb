require mojomail.inc

SUMMARY = "IMAP sync transport for Open webOS"

DEPENDS += "mojomail-common c-ares"

PV = "2.0.0-99+git${SRCPV}"
SRCREV = "3a433c00b37dbfc45210b30248b0b30a1503230e"

inherit webos_filesystem_paths
inherit webos_system_bus

S = "${WORKDIR}/git/imap"

FILES_${PN} += "${webos_accttemplatesdir}"
