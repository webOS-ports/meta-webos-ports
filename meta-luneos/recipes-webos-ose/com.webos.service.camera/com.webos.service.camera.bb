# Copyright (c) 2019-2024 LG Electronics, Inc.

SUMMARY = "Camera service framework to control camera devices"
AUTHOR = "Kalaimani K <kalaimani.k@lge.com>"
SECTION = "webos/services"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 json-c alsa-lib pmloglib udev"

WEBOS_VERSION = "1.0.0-42_6eb6cb0e6cd050c98a7773c8824a01eb71f78ea9"
PR = "r12"

inherit webos_cmake 
inherit pkgconfig
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus
inherit webos_machine_impl_dep

# depends on edgeai-vision
PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'webos-aiframework', d)}\
"

PACKAGECONFIG[webos-aiframework] = "-DWITH_AIFRAMEWORK=ON,-DWITH_AIFRAMEWORK=OFF,edgeai-vision"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.camera.service"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm ${D}${sysconfdir}/systemd/system/com.webos.service.camera.service
}

COMPATIBLE_MACHINE = "(.*)"
