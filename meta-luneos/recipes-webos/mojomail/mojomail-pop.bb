require mojomail.inc

SUMMARY = "POP3 email transport service for Open webOS"

DEPENDS += "mojomail-common c-ares"

inherit webos_filesystem_paths
inherit webos_system_bus

S = "${WORKDIR}/git/pop"

FILES_${PN} += "${webos_accttemplatesdir}"
