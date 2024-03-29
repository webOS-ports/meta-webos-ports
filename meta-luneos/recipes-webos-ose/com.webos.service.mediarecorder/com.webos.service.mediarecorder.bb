# Copyright (c) 2023-2024 LG Electronics, Inc.

SUMMARY = "Media Recorder Service"
AUTHOR = "Sungho Lee <shl.lee@lge.com>"
SECTION = "webos/services"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 pmloglib nlohmann-json"

WEBOS_VERSION = "1.0.0-2_6e79a8bbf649cfd9e04e9aeb1555a416d57487cb"
PR = "r0"

inherit webos_cmake
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.mediarecorder.service"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm ${D}${sysconfdir}/systemd/system/com.webos.service.mediarecorder.service
}


# Build a native app for testing the media recorder
PACKAGECONFIG[test-apps] = "-DWITH_CAMERA_TEST=ON,-DWITH_CAMERA_TEST=OFF, webos-wayland-extensions mesa jpeg, ${PN}-test-apps"

PACKAGES += "${PN}-test-apps"

RDEPENDS:${PN}-test-apps = "${PN}"

FILES:${PN}-test-apps = "${webos_applicationsdir}"
