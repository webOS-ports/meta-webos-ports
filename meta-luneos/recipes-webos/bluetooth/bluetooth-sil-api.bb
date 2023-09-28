# Copyright (c) 2014-2023 LG Electronics, Inc.

SUMMARY = "webOS Bluetooth SIL API"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=2763f3ed850f8412903ea776e0526bea \
    file://oss-pkg-info.yaml;md5=e07d738b032e6351a6608e81ea1b1b49 \
"

DEPENDS = "glib-2.0"

WEBOS_VERSION = "1.0.0-26_d1236c28f289d0e40f4d26c5d73ad198158e62bb"
PR = "r4"

PV = "1.0.0-26+git${SRCPV}"
SRCREV = "d1236c28f289d0e40f4d26c5d73ad198158e62bb"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DWEBOS_CONFIG_BUILD_TESTS:BOOL=TRUE"

PACKAGES =. "${PN}-tests "
FILES:${PN}-tests = "${bindir}/bluetooth-sil-tester"

ALLOW_EMPTY:${PN} = "1"
