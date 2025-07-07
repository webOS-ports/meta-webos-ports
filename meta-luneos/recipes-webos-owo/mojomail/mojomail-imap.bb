require mojomail.inc

SUMMARY = "IMAP sync transport for Open webOS"

DEPENDS += "mojomail-common c-ares"

inherit webos_filesystem_paths
inherit webos_system_bus

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/imap"

FILES:${PN} += "${webos_accttemplatesdir}"
