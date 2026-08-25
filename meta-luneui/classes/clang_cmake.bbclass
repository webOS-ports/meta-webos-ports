# Copyright (c) 2023 LG Electronics, Inc.

inherit webos_cmake
inherit clang_libc

# clang-native brings the compiler but not ld.lld, which ships in lld-native.
# Without it -fuse-ld=${STAGING_BINDIR_NATIVE}/ld.lld points at a file that was
# never staged, and clang reports it as a bad linker *name* rather than a
# missing path, which is misleading:
#   clang++: error: invalid linker name in argument
#   '-fuse-ld=.../recipe-sysroot-native/usr/bin/ld.lld'
DEPENDS:append = " clang-native lld-native"

OECMAKE_C_COMPILER = "clang"
OECMAKE_CXX_COMPILER = "clang++"

LIBCBE_DIR = "${libdir}/cbe"

CLANG_DEPENDENCY_SUFFIX = "-clang"

TOOLCHAIN_OPTIONS = "\
    --sysroot=${STAGING_DIR_TARGET} \
    --target=${TARGET_SYS} \
    -stdlib=libc++ \
    -fuse-ld=${STAGING_BINDIR_NATIVE}/ld.lld \
    -nostdinc++ \
    -isystem ${STAGING_INCDIR}/c++/v1/ \
    -Wl,-L${STAGING_DIR_TARGET}/${LIBCBE_DIR} \
    -Wl,-rpath,${LIBCBE_DIR} \
    -Wno-unused-command-line-argument \
    -D_LIBCPP_HAS_NO_VENDOR_AVAILABILITY_ANNOTATIONS \
"

TOOLCHAIN_OPTIONS:append = " ${@bb.utils.contains('USE_WEBRUNTIME_LIBCXX', '1', '-D_LIBCPP_ABI_UNSTABLE', '', d)}"

# The same as in
# https://github.com/kraj/meta-clang/commit/4cf1e9e0baf30568851c6646510d18bad50c613b
# but applied without toolchain-clang override which this doesn't use
# fixes:
# http://gecko.lge.com:8000/Builds/Details/1431440
DEBUG_PREFIX_MAP:remove = "-fcanon-prefix-map"
