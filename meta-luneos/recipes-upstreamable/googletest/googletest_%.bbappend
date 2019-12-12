# Copyright (c) 2015-2017 LG Electronics, Inc.

WEBOS_NO_STATIC_LIBRARIES_WHITELIST = "libgtest.a libgtest_main.a"

# GTest developers recommend to use source code instead of linking
# against a prebuilt library.
do_install_append() {
    mkdir ${B}/fused-src || true
    ${S}/googletest/scripts/fuse_gtest_files.py ${B}/fused-src
    cp -vf ${S}/googletest/src/gtest_main.cc ${B}/fused-src/gtest/

    install -d ${D}${prefix}/src/gtest/src
    install -d ${D}${prefix}/src/gtest/cmake

    install -v -m 0644 ${B}/fused-src/gtest/* ${D}${prefix}/src/gtest/src
    install -v -m 0644 ${S}/googletest/CMakeLists.txt ${D}${prefix}/src/gtest
    install -v -m 0644 ${S}/googletest/cmake/* ${D}${prefix}/src/gtest/cmake
    install -v -m 0644 ${S}/googletest/cmake/gtest.pc.in ${D}${prefix}/src/gtest

    install -d ${D}${bindir}/gtest
    install -v -m 0755 ${S}/googletest/test/*.py ${D}${bindir}/gtest
}

sysroot_stage_all_append() {
    sysroot_stage_dir ${D}${prefix}/src ${SYSROOT_DESTDIR}${prefix}/src
}

# Backport http://lists.openembedded.org/pipermail/openembedded-devel/2019-December/203572.html
# until it's in meta-oe/master
do_configure_prepend() {
    # explicitly use python3
    # the scripts are already python3 compatible since https://github.com/google/googletest/commit/d404af0d987a9c38cafce82a7e26ec8468c88361 and other fixes like this
    # but since this oe-core change http://git.openembedded.org/openembedded-core/commit/?id=5f8f16b17f66966ae91aeabc23e97de5ecd17447
    # there isn't python in HOSTTOOLS so "env python" fails
    sed -i 's@^#!/usr/bin/env python$@#!/usr/bin/env python3@g' ${S}/googlemock/scripts/*py ${S}/googlemock/scripts/generator/*py ${S}/googlemock/scripts/generator/cpp/*py ${S}/googlemock/test/*py ${S}/googletest/scripts/*py ${S}/googletest/test/*py
}

FILES_${PN} += "${bindir}/gtest"
FILES_${PN}-dev += "${prefix}/src"
