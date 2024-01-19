# Copyright (c) 2013-2023 LG Electronics, Inc.

DESCRIPTION = "Notification Manager"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib boost libxml++"

WEBOS_VERSION = "1.0.0-26_44ada0c03140ee74a73a0e41b8f506e79c58d97f"
PR = "r11"

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_systemd


SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Settings.cpp-Make-org.webosports-privileged-as-well.patch \
    file://0002-Revert-97e68e38b489ab103e68b63672b5444ee7a05d49.patch \
    file://0003-com.webos.notification.role.json.in-Fix-permission-i.patch \
    file://0004-NotificationService.h-Add-back-bits-required-by-Lune.patch \
    file://0005-com.webos.notification.perm.json-Fix-incorrect-value.patch \
"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "notificationmgr.service.in"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm ${D}${sysconfdir}/systemd/system/notificationmgr.service
}

FILES:${PN} += "${webos_prefix}"
