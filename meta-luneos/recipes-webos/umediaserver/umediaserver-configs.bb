# Copyright (c) 2014-2023 LG Electronics, Inc.

SUMMARY = "umediaserver configs installation"
AUTHOR = "Sujeet Nayak <Sujeet.nayak@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

WEBOS_VERSION = "1.0.0-15_48cb6a1271fbb3c08f8503da81047b28b1ef8c7c"
PR = "r7"

PV = "1.0.0-15+git${SRCPV}"
SRCREV = "48cb6a1271fbb3c08f8503da81047b28b1ef8c7c"

inherit pkgconfig
inherit webos_cmake
inherit webos_filesystem_paths
inherit webos_public_repo

PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OECMAKE += "-DWEBOS_INSTALL_CONFCAPSDIR:STRING=${webos_frameworksdir}"
EXTRA_OECMAKE += "-DWEBOS_TARGET_MACHINE:STRING=${MACHINE}"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES:${PN} += "${webos_frameworksdir}/umediaserver/*"
