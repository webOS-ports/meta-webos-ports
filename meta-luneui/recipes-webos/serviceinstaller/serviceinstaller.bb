# Copyright (c) 2012-2023 LG Electronics, Inc.

SUMMARY = "An extensible object oriented component used to add service components to webOS"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/devel"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"
DEPENDS = "librolegen glib-2.0 libpbnjson luna-service2"

WEBOS_VERSION = "2.0.0-4_13eaf63ab5ec5c4dd62b02f176c0b60894251b1c"
PR = "r6"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit pkgconfig

inherit features_check
# luna-service2 depends on systemd
REQUIRED_DISTRO_FEATURES = "systemd"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Make-sure-generated-service-files-get-a-.service-suf.patch \
"
S = "${WORKDIR}/git"

ALLOW_EMPTY:${PN} = "1"
