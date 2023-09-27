# Copyright (c) 2019-2023 LG Electronics, Inc.

SUMMARY = "g-camera-pipeline is a player which uses GStreamer"
AUTHOR = "Kalaimani K <kalaimani.k@lge.com>"
SECTION = "webos/media"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit pkgconfig

PR = "r14"

DEPENDS = "boost gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad pkgconfig umediaserver media-resource-calculator com.webos.service.camera webos-wayland-extensions"
DEPENDS:append:rpi = " userland"

COMPATIBLE_MACHINE = "^qemux86$|^qemux86-64$|^raspberrypi3$|^raspberrypi3-64$|^raspberrypi4$|^raspberrypi4-64$"

WEBOS_VERSION = "1.0.0-gav.40_48def9addb5dcb0f408137c5f8c34c33d799ab90"

PV = "1.0.0-gav.40+git${SRCPV}"
SRCREV = "48def9addb5dcb0f408137c5f8c34c33d799ab90"

WEBOS_GIT_PARAM_BRANCH = "@gav"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/*.so"
FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"
