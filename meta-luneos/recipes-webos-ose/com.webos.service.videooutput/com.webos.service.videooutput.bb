# Copyright (c) 2018-2024 LG Electronics, Inc.

SUMMARY = "Service which controls video output"
AUTHOR = "Kwanghee Lee <ekwang.lee@lge.com>"
SECTION = "webos/services"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

VIRTUAL-RUNTIME_val-impl ??= "videooutput-adaptation-layer-mock"
DEPENDS = "glib-2.0 luna-service2 pmloglib libpbnjson val-impl"
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_val-impl}"
WEBOS_VERSION = "1.0.0-13_931b3e59260a97ea0741ebbd1d84fd30b405b484"
PR = "r2"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit pkgconfig
inherit webos_test_provider

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}\
    file://0001-Add-trustLevel.patch \
"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.videooutput.service"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm ${D}${sysconfdir}/systemd/system/videooutputd.service
}

S = "${WORKDIR}/git"
