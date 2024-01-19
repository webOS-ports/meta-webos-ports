# Copyright (c) 2020-2023 LG Electronics, Inc.

SUMMARY = "Mediacontroller service"
AUTHOR = "Sujeet Nayak <Sujeet.nayak@lge.com>"
SECTION = "webos/services"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 json-c pmloglib"

WEBOS_VERSION = "1.0.0-28_4cecd9e728e00db17e4e2a8707bad36d133f7618"
PR = "r6"

inherit webos_cmake
inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_machine_impl_dep
inherit webos_system_bus

SRC_URI = " \
    ${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-com.webos.service.mediacontroller-Make-it-work-gener.patch \
    file://0002-com.webos.service.mediacontroller.role.json.in-Fix-o.patch \
    "
S = "${WORKDIR}/git"
inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.mediacontroller.service"

FILES:${PN} += "${libdir}/*.so"
FILES_SOLIBSDEV = ""
