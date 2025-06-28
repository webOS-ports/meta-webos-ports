# Copyright (c) 2018-2023 LG Electronics, Inc.

SUMMARY = "AudioOutputd adaptation layer (UMI) API definition and test harness"
AUTHOR = "Sushovan G <sushovan.g@lge.com>"
SECTION = "webos/libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://CMakeLists.txt;beginline=1;endline=15;md5=059bf74645cdef24f5e9a0ccb2a4cb94 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

inherit pkgconfig
inherit webos_cmake
inherit webos_test_provider
inherit webos_public_repo
inherit webos_enhanced_submissions

DEPENDS = "glib-2.0 pmloglib libpbnjson alsa-lib"

WEBOS_VERSION = "1.0.0-10_5562fabff79cce1fcd85034ff0273fc56cb806cd"
PR = "r4"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

#UMI only supports RPI and qemux86 values as per https://github.com/webosose/umi/blob/c8f08f898ad5940d06f4b15c81dab740808c55b9/src/devicecapability.h#L49
EXTRA_OECMAKE:append:qemux86 = " -DWEBOS_SOC:STRING=qemux86"
EXTRA_OECMAKE:append:qemux86-64 = " -DWEBOS_SOC:STRING=qemux86"
EXTRA_OECMAKE:append:aarch64 = " -DWEBOS_SOC:STRING=RPI"
EXTRA_OECMAKE:append:armv7a = " -DWEBOS_SOC:STRING=RPI"
EXTRA_OECMAKE:append:armv7ve = " -DWEBOS_SOC:STRING=RPI"
