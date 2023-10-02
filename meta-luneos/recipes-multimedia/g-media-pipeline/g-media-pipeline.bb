# Copyright (c) 2018-2023 LG Electronics, Inc.

SUMMARY = "G media pipeline is a media pipeline which uses GStreamer"
AUTHOR = "Sujeet Nayak <Sujeet.nayak@lge.com>"
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

PR = "r18"
DEPENDS = "boost gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad umediaserver media-resource-calculator webos-wayland-extensions"
RDEPENDS:${PN} = "gstreamer1.0-plugins-webosrs"
DEPENDS:append:rpi = " virtual/libomxil"

COMPATIBLE_MACHINE = "^qemux86$|^qemux86-64$|^raspberrypi3$|^raspberrypi3-64$|^raspberrypi4$|^raspberrypi4-64$"

WEBOS_VERSION = "1.0.0-gav.48_3ba1c574047904a87182f34482eea6197bf5a48f"

PV = "1.0.0-gav.48+git"
SRCREV = "3ba1c574047904a87182f34482eea6197bf5a48f"

WEBOS_GIT_PARAM_BRANCH = "@gav"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/*.so"
