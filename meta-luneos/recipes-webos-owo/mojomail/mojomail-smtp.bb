require mojomail.inc

SUMMARY = "SMTP transport service for sending emails"

DEPENDS += "mojomail-common c-ares"

inherit webos_system_bus

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/smtp"

SRC_URI += "file://0001-Fix-build-with-gcc-12.patch;patchdir=.."
