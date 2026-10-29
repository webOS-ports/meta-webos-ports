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
inherit webos_ports_ose_repo
inherit webos_machine_dep
inherit pkgconfig

# media-resource-calculator since submissions 47 isn't usable for any other MACHINE than
# raspberrypi3
# raspberrypi3-64
# raspberrypi4
# raspberrypi4-64
# qemux86
# qemux86-64
#In LuneOS we want this for all machines
#COMPATIBLE_MACHINE = "^qemux86$|^qemux86-64$|^raspberrypi3$|^raspberrypi3-64$|^raspberrypi4$|^raspberrypi4-64$"

PR = "r20"
DEPENDS = "boost gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad umediaserver media-resource-calculator webos-wayland-extensions"
RDEPENDS:${PN} = "gstreamer1.0-plugins-webosrs"
DEPENDS:append:rpi = " virtual/libomxil"

# Built from the webOS-ports fork rather than webosose/g-media-pipeline. Its
# webOS-ports/webOS-OSE branch starts from submissions/gav.59 and carries the
# three patches this recipe used to apply as commits.
PV = "1.0.0-gav.59+git"
SRCREV = "7c64b4f4536393c53e2d03c6880209e01d5ababa"

WEBOS_REPO_NAME = "g-media-pipeline"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

