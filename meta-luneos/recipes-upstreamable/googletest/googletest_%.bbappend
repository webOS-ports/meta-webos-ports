# Copyright (c) 2015-2017 LG Electronics, Inc.

WEBOS_NO_STATIC_LIBRARIES_WHITELIST = "libgtest.a libgtest_main.a"

# GTest developers recommend to use source code instead of linking
# against a prebuilt library.
do_install:append() {
    rm -rf ${B}/fused-src
    mkdir ${B}/fused-src
    ${S}/googletest/scripts/fuse_gtest_files.py ${B}/fused-src
    cp -vf ${S}/googletest/src/gtest_main.cc ${B}/fused-src/gtest/

    install -d ${D}${prefix}/src/gtest/src
    install -d ${D}${prefix}/src/gtest/cmake

    install -v -m 0644 ${B}/fused-src/gtest/* ${D}${prefix}/src/gtest/src
    # start with GOOGLETEST_VERSION from the root CMakeLists.txt
    grep '^set(GOOGLETEST_VERSION' ${S}/CMakeLists.txt > ${D}${prefix}/src/gtest/CMakeLists.txt
    cat  ${S}/googletest/CMakeLists.txt >> ${D}${prefix}/src/gtest/CMakeLists.txt
    install -v -m 0644 ${S}/googletest/cmake/* ${D}${prefix}/src/gtest/cmake
    install -v -m 0644 ${S}/googletest/cmake/gtest.pc.in ${D}${prefix}/src/gtest

    install -d ${D}${bindir}/gtest
    install -v -m 0755 ${S}/googletest/test/*.py ${D}${bindir}/gtest
}

sysroot_stage_all:append() {
    sysroot_stage_dir ${D}${prefix}/src ${SYSROOT_DESTDIR}${prefix}/src
}

FILES:${PN} += "${bindir}/gtest"
FILES:${PN}-dev += "${prefix}/src"
