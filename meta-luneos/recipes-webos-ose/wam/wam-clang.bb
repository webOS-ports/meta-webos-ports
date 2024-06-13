# Copyright (c) 2024 LG Electronics, Inc.

require wam.inc

inherit clang_cmake

BPN = "wam"

PROVIDES = "virtual/webappmanager-webos"

WEBOS_REPO_NAME = "wam"

PR = "${INC_PR}.7"

PACKAGECONFIG += "${@bb.utils.contains('USE_WEBRUNTIME_LIBCXX', '1', 'webruntime-libcxx', 'system-libcxx', d)}"
PACKAGECONFIG[webruntime-libcxx] = ",,chromium-toolchain-native chromium-stdlib"
PACKAGECONFIG[system-libcxx] = ",,libcxx"
DEPENDS:remove = "jsoncpp"
DEPENDS += "jsoncpp-clang"
DEPENDS:remove = "gtest googletest"
DEPENDS += "googletest-clang"

CXXFLAGS +="-I${STAGING_INCDIR}/cbe"

OECMAKE_CXX_FLAGS += "\
    -Wno-error=unused-command-line-argument \
    -Wno-error=inconsistent-missing-override \
    -Wno-format \
"

do_configure:prepend() {
    ln -snf jsoncpp-clang.pc ${STAGING_LIBDIR}/pkgconfig/jsoncpp.pc
}

do_compile:prepend() {
    sed -i '/^libdir=.*\/lib$/ s/$/\/cbe/' ${STAGING_LIBDIR}/pkgconfig/gtest.pc
    sed -i '/^libdir=.*\/lib$/ s/$/\/cbe/' ${STAGING_LIBDIR}/pkgconfig/gmock.pc
    sed -i '/^libdir=.*\/lib$/ s/$/\/cbe/' ${STAGING_LIBDIR}/pkgconfig/gmock_main.pc
    sed -i '/^libdir=.*\/lib$/ s/$/\/cbe/' ${STAGING_LIBDIR}/pkgconfig/gtest_main.pc
    rm -rf ${STAGING_LIBDIR}/libgtest*.a
    rm -rf ${STAGING_LIBDIR}/libgmock*.a
}

# pass dyld-prefix with usrmerge otherwise the default loader from clang++ will be non-existent (on target)
# /lib64/ld-linux-x86-64.so.2 instead of expected /usr/lib/ld-linux-x86-64.so.2 for qemux86-64
TUNE_CCARGS:append = "${@bb.utils.contains("DISTRO_FEATURES", "usrmerge", " --dyld-prefix=/usr", "", d)}"
