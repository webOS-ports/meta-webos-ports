# Copyright (c) 2020-2023 LG Electronics, Inc.

SUMMARY = "Intent Manager"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson"

WEBOS_VERSION = "1.0.0-17_ea416580431307921b4846e9421af5d22a8d62d5"
PR = "r3"

inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_public_repo
inherit webos_enhanced_submissions

# [http://gpro.lge.com/c/webosose/com.webos.service.intent/+/348184 Fix luna-service2 usage]
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Fix-luna-service2-usage.patch \
"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.intent.service.in"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm ${D}${sysconfdir}/systemd/system/com.webos.service.intent.service
}
