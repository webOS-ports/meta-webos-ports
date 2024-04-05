# Copyright (c) 2014-2024 LG Electronics, Inc.

SUMMARY = "webOS Configuration Service"
AUTHOR  = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "luna-service2 pmloglib glib-2.0 libpbnjson gtest"
RDEPENDS:${PN} += "configd-data"

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd
inherit webos_public_repo
inherit webos_enhanced_submissions

WEBOS_VERSION = "1.2.0-20_d4fedddd71d73ce997cabde29d31f821c67a535b"
PR = "r19"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "configd.service"

PACKAGES =+ "${PN}-tests"
FILES:${PN}-tests = "${libexecdir}/tests/*"
FILES:${PN} += "${webos_sysbus_datadir}"

# configd/1.2.0-20/git/src/libconfigd/libconfigd.c:335:17: error: passing argument 4 of 'LSCall' from incompatible pointer type [-Wincompatible-pointer-types]
# configd/1.2.0-20/git/src/libconfigd/libconfigd.c:870:13: error: passing argument 4 of 'LSCallOneReply' from incompatible pointer type [-Wincompatible-pointer-types]
# configd/1.2.0-20/git/src/libconfigd/libconfigd.c:939:29: error: passing argument 4 of 'LSCallOneReply' from incompatible pointer type [-Wincompatible-pointer-types]
# configd/1.2.0-20/git/src/libconfigd/libconfigd.c:970:25: error: passing argument 4 of 'LSCallOneReply' from incompatible pointer type [-Wincompatible-pointer-types]
CFLAGS += "-Wno-error=incompatible-pointer-types"
