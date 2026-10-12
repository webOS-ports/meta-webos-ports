# Copyright (c) 2014-2025 LG Electronics, Inc.

SUMMARY = "webOS Bluetooth SIL API"
AUTHOR = "Muralidhar N <muralidhar.n@lge.com>"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=2763f3ed850f8412903ea776e0526bea \
    file://oss-pkg-info.yaml;md5=e07d738b032e6351a6608e81ea1b1b49 \
"

DEPENDS = "glib-2.0"

# Built from the webOS-ports fork: webosose submissions/26 with the LuneOS
# changes as commits, the CMake 4 fix this recipe used to patch in among them.
# Plain SRCREV: submission tags are a webosose convention.
PV = "1.0.0-26+git"
SRCREV = "80746873c94cfafa4b83ef2d4dd2a94bf2f868ee"
PR = "r5"

inherit webos_component
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

EXTRA_OECMAKE += "-DWEBOS_CONFIG_BUILD_TESTS:BOOL=TRUE"

PACKAGES =. "${PN}-tests "
FILES:${PN}-tests = "${bindir}/bluetooth-sil-tester"

ALLOW_EMPTY:${PN} = "1"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
