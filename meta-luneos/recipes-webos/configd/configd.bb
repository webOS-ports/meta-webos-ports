# Copyright (c) 2014-2021 LG Electronics, Inc.

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

PV = "1.2.0-15+git${SRCPV}"
SRCREV = "8174a13520e4564252d7568b5c2761fc9e1d99cb"

#WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-Add-systemd-service-file.patch"
S = "${WORKDIR}/git"

PACKAGES =+ "${PN}-tests"
FILES:${PN}-tests = "${libexecdir}/tests/*"
FILES:${PN} += "${webos_sysbus_datadir}"
