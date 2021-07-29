require mojomail.inc

SUMMARY = "IMAP sync transport for Open webOS"

DEPENDS += "mojomail-common c-ares"

inherit webos_filesystem_paths
inherit webos_system_bus

S = "${WORKDIR}/git/imap"

FILES:${PN} += "${webos_accttemplatesdir}"
