# Copyright (c) 2012-2013 LG Electronics, Inc.

SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "jemalloc db8 boost curl libpalmsocket libsandbox pmloglib icu json-c glib-2.0 luna-service2"

PV = "2.0.0-99+git"
SRCREV = "3e95ae94e5d59a3d895ebe046a813f07f4f039b1"

FILESEXTRAPATHS:prepend := "${THISDIR}/mojomail:"

# The sync fix (webOS-ports/mojomail#13) lives on
# herrie/fix-sync-internet-requirement, not on the branch
# webos_ports_ose_repo defaults to, so the SRCREV above is not reachable from
# webOS-ports/webOS-OSE. Drop this line once that branch is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fix-sync-internet-requirement"
inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig

WEBOS_REPO_NAME = "mojomail"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# -fpermissive used to be needed here, but only ever for three g_base64_encode
# calls that cast to "(unsigned guchar*)" -- guchar is already unsigned char, so
# that is not a type, and GCC rejects the conversion outright without the flag.
# The casts are fixed upstream now (const guchar*, as the IMAP copy of the same
# call always had), and all 269 translation units compile clean without it, so
# the whole tree gets real type checking back.
