# Copyright (c) 2018-2024 LG Electronics, Inc.

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
inherit webos_enhanced_submissions
inherit webos_machine_dep
inherit pkgconfig

PR = "r19"
DEPENDS = "boost gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad umediaserver media-resource-calculator webos-wayland-extensions"
RDEPENDS:${PN} = "gstreamer1.0-plugins-webosrs"
DEPENDS:append:rpi = " virtual/libomxil"

#In LuneOS we want this for all machines
#COMPATIBLE_MACHINE = "^qemux86$|^qemux86-64$|^raspberrypi3$|^raspberrypi3-64$|^raspberrypi4$|^raspberrypi4-64$"

WEBOS_VERSION = "1.0.0-gav.53_4e91a56809eb88d4b45d0121f3912a040995a141"

WEBOS_GIT_PARAM_BRANCH = "@gav"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}\ 
    file://0001-Add-generic-config.patch \
"
S = "${WORKDIR}/git"

