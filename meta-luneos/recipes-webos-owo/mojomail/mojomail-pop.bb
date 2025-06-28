require mojomail.inc

SUMMARY = "POP3 email transport service for Open webOS"

DEPENDS += "mojomail-common c-ares"

inherit webos_filesystem_paths
inherit webos_system_bus

S = "${UNPACKDIR}/git/pop"

FILES:${PN} += "${webos_accttemplatesdir}"
