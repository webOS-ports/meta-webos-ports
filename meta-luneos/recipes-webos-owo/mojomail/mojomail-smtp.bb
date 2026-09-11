require mojomail.inc

SUMMARY = "SMTP transport service for sending emails"

DEPENDS += "mojomail-common c-ares"

inherit webos_system_bus

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/smtp"

# 0001-Fix-build-with-gcc-12.patch is gone: it only ever touched the three
# g_base64_encode calls below, and it "fixed" them by deleting the bogus
# (unsigned guchar*) cast entirely, which left a const char* that GCC still
# refuses without -fpermissive. Upstream now casts to (const guchar*) instead,
# which is correct on its own and let -fpermissive come out of mojomail.inc.
