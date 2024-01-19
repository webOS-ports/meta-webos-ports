# Copyright (c) 2020-2023 LG Electronics, Inc.

SUMMARY = "Memory Manager"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 glib-2.0-native luna-service2 libpbnjson pmloglib"

WEBOS_VERSION = "1.0.0-57_5d320c6c5fb80fce406b5384351985cda0808914"
PR = "r10"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "memorymanager.service.in"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm ${D}${sysconfdir}/systemd/system/memorymanager.service
}
