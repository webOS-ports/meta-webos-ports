# Copyright (c) 2019-2023 LG Electronics, Inc.

SUMMARY = "Camera service framework to control camera devices"
AUTHOR = "Kalaimani K <kalaimani.k@lge.com>"
SECTION = "webos/services"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 json-c alsa-lib pmloglib udev"

# depends on edgeai-vision and opencv for face recognition
#DEPENDS += "edgeai-vision jpeg opencv"
#REQUIRED_DISTRO_FEATURES = "webos-aiframework"

WEBOS_VERSION = "1.0.0-36_9ffdd45eb7385bd8447117cc061ac9e73a819d57"
PR = "r7"

PV = "1.0.0-36+git${SRCPV}"
SRCREV = "9ffdd45eb7385bd8447117cc061ac9e73a819d57"

inherit webos_cmake 
inherit pkgconfig
inherit webos_public_repo
inherit webos_system_bus
inherit webos_machine_impl_dep

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
