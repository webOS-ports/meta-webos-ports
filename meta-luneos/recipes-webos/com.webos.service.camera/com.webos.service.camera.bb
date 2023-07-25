# Copyright (c) 2019-2023 LG Electronics, Inc.

SUMMARY = "Camera service framework to control camera devices"
AUTHOR = "Premalatha M V S <premalatha.mvs@lge.com>"
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

WEBOS_VERSION = "1.0.0-35_8ec5e64470df87173c8013e7661d96541c6d544c"
PR = "r7"

PV = "1.0.0-35+git${SRCPV}"
SRCREV = "8ec5e64470df87173c8013e7661d96541c6d544c"

inherit webos_cmake pkgconfig
inherit webos_public_repo
inherit webos_system_bus
inherit webos_machine_impl_dep
#inherit webos_daemon

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(.*)"

FILES:${PN} += "${libdir}/*.so"
FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so"
