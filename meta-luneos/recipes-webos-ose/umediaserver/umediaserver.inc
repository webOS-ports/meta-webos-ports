# Copyright (c) 2014-2024 LG Electronics, Inc.

SUMMARY = "webOS uMediaserver daemon and utilities"
AUTHOR = "Sujeet Nayak <Sujeet.nayak@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 libpbnjson libconfig swig-native libxml2 luna-service2 pmloglib boost luna-prefs"
DEPENDS += "gstreamer1.0 gstreamer1.0-plugins-base"
DEPENDS += "pmtrace"
RDEPENDS:${PN} = "umediaserver-configs"

INC_PR = "r26"

inherit pkgconfig
inherit webos_cmake
inherit webos_system_bus
inherit python3-dir
inherit python3native
inherit webos_public_repo
inherit webos_enhanced_submissions

do_configure:prepend() {
    sed -i 's@add_subdirectory(test/python)@#disabled until updated to work with python3 add_subdirectory(test/python)@g' ${S}/CMakeLists.txt
}

WEBOS_VERSION = "1.0.0-gav.40_37ef88f5f401ced68d9296ba425fffe74fbfa0ac"

WEBOS_GIT_PARAM_BRANCH = "@gav"
# http://gpro.lge.com/c/webosose/umediaserver/+/396224 Fix build with boost-1.84.0
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Fix-build-with-boost-1.84.0.patch \
"
S = "${WORKDIR}/git"

PACKAGECONFIG = ""
PACKAGECONFIG[com.webos.service.videooutput] = "-DUSE_VIDEOOUTPUTD:BOOL=TRUE,-DUSE_VIDEOOUTPUTD:BOOL=FALSE,,com.webos.service.videooutput"

# umediaserver-python contains the Python bindings
PACKAGES =+ "${PN}-python"

FILES:${PN}-python = "${libdir}/${PYTHON_DIR}/site-packages/uMediaServer/* ${datadir}/${BPN}/python/"

# Use append, because umediaserver-clang uses:
# PACKAGECONFIG += "${@bb.utils.contains('USE_WEBRUNTIME_LIBCXX', '1', 'webruntime-libcxx', 'system-libcxx', d)}"
PACKAGECONFIG:append:qemuall = " com.webos.service.videooutput"

do_install:append() {
    install -m 755 -o media -g media -v -d ${D}${webos_execstatedir}/ums
}
