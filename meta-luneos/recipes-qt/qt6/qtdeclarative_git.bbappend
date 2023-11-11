# Copyright (c) 2014-2023 LG Electronics, Inc.

inherit webos_qt_global

EXTENDPRAUTO:append = "webos84"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PATCHTOOL = "git"

# Upstream-Status: Submitted
# NOTE: Increase maxver when upgrading Qt version
SRC_URI:append = " \
    file://0001-Check-if-a-device-in-knownPointingDevices-is-destroy.patch;maxver=6.5.3 \
"

SRC_URI:append:armv7a = " \
    file://0003-Avoid-mat4-in-struct-shaders-on-armv7.patch \
"

# Supplement tool for qmllint
inherit webos_qmake6_paths
DEPENDS:append:class-native = " python3-regex-native"
SRC_URI:append:class-native = " file://qmllint-supplement.py"
do_install:append:class-native() {
    install -m 755 ${WORKDIR}/qmllint-supplement.py ${D}${OE_QMAKE_PATH_QT_BINS}
}

# TODO: To workaround the build issue where a recipe that depends on
# qtdeclarative fails at do_configure with CMake errors like:
# The imported target "Qt6::qmltyperegistrar" references the file
# ".../recipe-sysroot/usr/libexec/qmltyperegistrar"
# The imported target "Qt6::qmldom" references the file
# ".../recipe-sysroot/usr/bin/qmldom"
SYSROOT_DIRS:append = " \
    ${bindir} \
    ${libexecdir} \
"
