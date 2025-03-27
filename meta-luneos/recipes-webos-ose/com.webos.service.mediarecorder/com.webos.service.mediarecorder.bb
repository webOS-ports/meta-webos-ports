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

# Record pipeline
DEPENDS += "boost gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad umediaserver media-resource-calculator"

WEBOS_VERSION = "1.0.0-17_255f198c5ee9a9ca5aec4445cd34d5c20086df9f"
PR = "r4"

inherit webos_cmake
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus
inherit pkgconfig
inherit features_check
ANY_OF_DISTRO_FEATURES = "vulkan opengl"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.mediarecorder.service"

# Build a native app for testing the media recorder
PACKAGECONFIG[test-apps] = "-DWITH_CAMERA_TEST=ON,-DWITH_CAMERA_TEST=OFF, webos-wayland-extensions virtual/mesa jpeg, ${PN}-test-apps"

PACKAGES += "${PN}-test-apps"

RDEPENDS:${PN}-test-apps = "${PN}"

FILES:${PN}-test-apps = "${webos_applicationsdir}"

# Calculate display plane resource
PACKAGECONFIG[use-display-resource] = "-DUSE_DISPLAY_RESOURCE:BOOL=True,-DUSE_DISPLAY_RESOURCE:BOOL=False,"

# Pro UMS
PACKAGECONFIG[pro-ums] = "-DPRO_UMS:BOOL=True,-DPRO_UMS:BOOL=False,"

PACKAGECONFIG = "test-apps"

PACKAGECONFIG:append = " use-display-resource"
