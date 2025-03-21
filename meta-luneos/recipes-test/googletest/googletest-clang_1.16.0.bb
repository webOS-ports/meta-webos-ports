# Copyright (c) 2023 LG Electronics, Inc.

inherit clang_cmake

require recipes-test/googletest/googletest_${PV}.bb
# Needed for meta-oe/recipes-test/googletest/googletest/gtest-ciso646.patch
FILESEXTRAPATHS:prepend = "${META_OE_LAYER}/recipes-test/googletest/googletest:"

PACKAGECONFIG += "${@bb.utils.contains('USE_WEBRUNTIME_LIBCXX', '1', 'webruntime-libcxx', 'system-libcxx', d)}"
PACKAGECONFIG[webruntime-libcxx] = ",,chromium-toolchain-native chromium-stdlib"
PACKAGECONFIG[system-libcxx] = ",,libcxx"

PROVIDES = ""

PR = "r1"

do_install:append() {
    install -d ${D}/${LIBCBE_DIR}
    mv ${D}${libdir}/*.a ${D}/${LIBCBE_DIR}

    rm -rf ${D}${includedir}
    rm -rf ${D}${libdir}/cmake
    rm -rf ${D}${libdir}/pkgconfig
}

FILES:${PN}-staticdev += "${LIBCBE_DIR}/*.a"
