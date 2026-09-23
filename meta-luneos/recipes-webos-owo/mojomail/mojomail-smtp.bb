require mojomail.inc

SUMMARY = "SMTP transport service for sending emails"

DEPENDS += "mojomail-common c-ares"

inherit webos_system_bus

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/smtp"
