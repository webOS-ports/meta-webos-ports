# Copyright (c) 2024-2025 LG Electronics, Inc.

require webruntime_120.bb

PROVIDES = "virtual/webruntime"

PR = "r3"

inherit clang_libc

GCC_CROSS_VER = "11.3.0"
DEPEXT = "${@bb.utils.contains('WEBRUNTIME_CLANG_STDLIB', '1', '', '-clang', d)}"

PACKAGECONFIG += "${@bb.utils.contains('USE_WEBRUNTIME_LIBCXX', '1', '', 'system-libcxx', d)}"
PACKAGECONFIG[system-libcxx] = ",,libcxx"

GN_ARGS_CLANG = "is_clang=true"

# Build Chromium with the system clang rather than the clang bundled in
# third_party/llvm-build.
#
# LuneOS deliberately builds webruntime against the shared system libc++ (the
# CBE) instead of Chromium's own: use_custom_libcxx=false, with
# -I${STAGING_INCDIR}/c++/v1 supplied through INCLUDE_PATH_LIBCXX. That worked
# while meta-clang and Chromium's bundled clang were the same vintage. On
# wrynose meta-clang is LLVM 22, and Chromium 120 bundles clang 18, which
# cannot parse libc++ 22 headers:
#   .../include/c++/v1/__type_traits/aligned_storage.h:41:59: error: use of
#   undeclared identifier '__builtin_clzg'
#   error: use of undeclared identifier '__GCC_CONSTRUCTIVE_SIZE'
# (__builtin_clzg/__builtin_ctzg and __GCC_*_SIZE all arrived in clang 19.)
#
# The two obvious alternatives are not available here. USE_WEBRUNTIME_LIBCXX=1
# would switch every -clang component to chromium-stdlib and
# chromium-toolchain-native, but those exist only as .inc files with no recipe
# providing them. use_custom_libcxx=true needs
# buildtools/third_party/libc++/trunk, which is stripped from our source drop.
#
# So point Chromium at clang-native, which is the same 22.1.8 that built the
# libc++ in the sysroot; compiler and standard library then match.
# clang_use_chrome_plugins must go with it - those plugins are ABI-tied to the
# bundled clang and will not load in a different one. treat_warnings_as_errors
# is already false in webruntime-common.inc, which matters across a jump from
# clang 18 to 22.
# lld-native as well as clang-native: Chromium links with -fuse-ld=lld, and
# with the bundled toolchain lld came from third_party/llvm-build. Now that we
# point clang_base_path at the sysroot, ld.lld has to be staged there too or
# the link fails with
#   clang++: error: invalid linker name in argument '-fuse-ld=lld'
# which is clang's wording for a linker it cannot resolve, not a bad flag.
DEPENDS += "clang-native lld-native"
GN_ARGS += "clang_base_path=\"${STAGING_DIR_NATIVE}${prefix}\""
GN_ARGS += "clang_use_chrome_plugins=false"

# clang 22 removed __builtin_ia32_vcvtph2ps256, which skcms uses for its F16C
# half-to-float path. Only that one builtin went - vcvtps2ph256, roundps256 and
# the gather builtins were all checked against clang 22 and still compile.
SRC_URI += "file://0001-skcms-use-_mm256_cvtph_ps-for-clang-22.patch"

# Perfetto uses the template disambiguator without a template argument list in
# six places, which clang now rejects. Every TU that includes
# base/trace_event/trace_event.h - most of base/ - fails without this.
SRC_URI += "file://0002-perfetto-drop-template-keyword-without-arg-list.patch"

# Don't use gold even when selected by default with ld-is-gold in DISTRO_FEATURES
# because liblttng_provider is built with default host linker (hosttools/ld.gold)
# and build fails because use_lld added --color-diagnostic which isn't recognized
# by host's ld.gold (it should be fixed by using the cross toolchain with lld).
# http://gecko.lge.com:8000/Errors/Details/648296
EXTRA_OEGN_GOLD = ""

GN_ARGS += "target_sysroot=\"${STAGING_DIR_TARGET}\""

INCLUDE_PATH_STDLIB = " \
    -I../../${MLPREFIX}recipe-sysroot${includedir}/c++/${GCC_CROSS_VER} \
    -I../../${MLPREFIX}recipe-sysroot${includedir}/c++/${GCC_CROSS_VER}/${TARGET_SYS} \
"

INCLUDE_PATH_LIBCXX_EXT = " \
    -I../../${MLPREFIX}recipe-sysroot${includedir}/c++/v1 \
"

INCLUDE_PATH_LIBCXX = "${@bb.utils.contains('USE_WEBRUNTIME_LIBCXX', '1', '', '${INCLUDE_PATH_LIBCXX_EXT}', d)}"
INCLUDE_PATH_LIBCXX += " \
    -I../../${MLPREFIX}recipe-sysroot${includedir}/cbe \
    -I../../${MLPREFIX}recipe-sysroot${includedir}/cbe/gmp \
    -I../../${MLPREFIX}recipe-sysroot${includedir}/media-resource-calculator-clang \
"

# tcmalloc build is broken with clang++ and -mthumb as shown in:
# http://gecko.lge.com:8000/Errors/Details/528000
# http://gecko.lge.com:8000/Errors/Details/527999
ARM_INSTRUCTION_SET = "arm"

CLANG_CXXFLAGS = ""

GN_ARGS += "${@bb.utils.contains('WEBRUNTIME_CLANG_STDLIB', '1', 'clang_use_stdlib=true clang_extra_cxxflags=\\\"${INCLUDE_PATH_STDLIB} ${TARGET_CC_ARCH} ${CLANG_CXXFLAGS}\\\"', 'clang_use_stdlib=false clang_extra_cxxflags=\\\"${INCLUDE_PATH_LIBCXX} ${TARGET_CC_ARCH} ${CLANG_CXXFLAGS}\\\"', d)}"

GN_ARGS += "webos_rpath=\"${libdir}/cbe\""

GN_ARGS += "${@'cc_wrapper=\\\"ccache \\\"' if bb.data.inherits_class('ccache', d) else ''}"

PACKAGECONFIG[umediaserver] = ",,umediaserver${DEPEXT}"
# enable_webm_video_codecs=true, as in webruntime.inc — this line re-declares the whole PACKAGECONFIG
# just to append ${DEPEXT} to the dependency, so it silently overrides the value set there. See the
# comment in webruntime.inc for why WebM has to be on for LuneOS.
PACKAGECONFIG[gstreamer] = "use_gst_media=true enable_webm_video_codecs=true,use_gst_media=false,g-media-pipeline${DEPEXT}"
PACKAGECONFIG[webos-codec] = "use_webos_codec=true,use_webos_codec=false,media-codec-interface${DEPEXT}"
PACKAGECONFIG[webos-camera] = "use_webos_camera=true,use_webos_camera=false, cambufferlib${DEPEXT}"

do_configure:prepend() {
    ln -snf umedia_api_clang.pc ${STAGING_DATADIR}/pkgconfig/umedia_api.pc

    # g-media-pipeline is optional for various webruntime configurations,
    # condition is needed to check if gmp-player-client-clang.pc is
    # available during configuration of webruntime.
    [ -f ${STAGING_DATADIR}/pkgconfig/gmp-player-client-clang.pc ] && \
        ln -snf gmp-player-client-clang.pc ${STAGING_DATADIR}/pkgconfig/gmp-player-client.pc
}
