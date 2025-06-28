require mojomail.inc

SUMMARY = "SMTP transport service for sending emails"

DEPENDS += "mojomail-common c-ares"

inherit webos_system_bus

S = "${UNPACKDIR}/git/smtp"

SRC_URI += "file://0001-Fix-build-with-gcc-12.patch;patchdir=.."
