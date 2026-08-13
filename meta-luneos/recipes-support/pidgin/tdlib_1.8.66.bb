SUMMARY = "TDLib -- cross-platform library for building Telegram clients"
DESCRIPTION = "Telegram Database Library. Built here as the backend for tdlib-purple, the \
libpurple Telegram plug-in."
HOMEPAGE = "https://core.telegram.org/tdlib"
SECTION = "libs"
LICENSE = "BSL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE_1_0.txt;md5=e4224ccaecb14d942c71d31bef20d78c"

DEPENDS = "openssl zlib gperf-native"

# Built from the monorepo's vendored copy rather than upstream, because that is the tree
# tdlib-purple is developed and tested against. SRCREV is shared with the plugin recipes -- see
# purple-synergy.inc for why it is pinned rather than AUTOREV.
SRC_URI = "git://github.com/Herrie82/webos-synergy-revival.git;branch=herrie/telegram-tdlib-1.8.66;protocol=https"
SRCREV = "23e8193c5ff9c83a7a9f275af2947d868d3d2ce2"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/messaging/telegram/plugin/tdlib-src"

inherit cmake

# TDLib cannot be cross-compiled in one pass. Headers like td/mtproto/mtproto_api.h do not exist
# in the source tree: they are produced by generator programs (tl_generate_mtproto,
# tl_generate_common, tdmime_auto, tl_generate_json) that TDLib builds and then RUNS. Cross-built
# generators cannot run on the builder, so the first cross attempt fails with
#
#     td/mtproto/Handshake.cpp:12:10: fatal error: td/mtproto/mtproto_api.h: No such file
#
# Upstream's answer is the prepare_cross_compiling target, driven from a native configure. This
# does that in-recipe rather than via tdlib-native, because the generators write their output into
# the SOURCE tree -- a separate native recipe would generate into its own WORKDIR, where this
# build would never see it.
#
# TD_GENERATE_SOURCE_FILES=ON keeps the pass cheap: TDLib skips its OpenSSL/zlib lookups and
# builds only the generators, not the library.
do_configure:prepend() {
    cmake -S ${S} -B ${WORKDIR}/generate \
        -DTD_GENERATE_SOURCE_FILES=ON \
        -DCMAKE_BUILD_TYPE=Release \
        -DCMAKE_C_COMPILER="${BUILD_CC}" \
        -DCMAKE_CXX_COMPILER="${BUILD_CXX}" \
        -DCMAKE_C_FLAGS="${BUILD_CFLAGS}" \
        -DCMAKE_CXX_FLAGS="${BUILD_CXXFLAGS}" \
        -DCMAKE_EXE_LINKER_FLAGS="${BUILD_LDFLAGS}"
    cmake --build ${WORKDIR}/generate -j ${@oe.utils.cpu_count()}
}

# Static libraries only. tdlib-purple links libtdclient/libtdcore/libtdapi/libtddb/libtdsqlite/
# libtde2e/libtdmtproto/libtdnet/libtdactor/libtdutils as archives, which is also how the ARM
# build in this monorepo consumes it.
EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DTD_ENABLE_LTO=OFF \
    -DBUILD_SHARED_LIBS=OFF \
"

# TDLib's generated translation units are individually large, so its peak memory per compile job
# is well above average -- roughly 1-2 GB. That is worth knowing, but it is NOT a reason to
# throttle by default: at the -j 24 this build sets globally that is ~50 GB peak against the
# 125 GB this builder has.
#
# An earlier version of this recipe pinned PARALLEL_MAKE = "-j 2" as a precaution against OOM on
# a small builder. That was a guess, and an expensive one: it overrode the global setting and
# made tdlib roughly twelve times slower than it needed to be. If you are building on something
# genuinely memory-constrained, set PARALLEL_MAKE in your local.conf rather than here, where it
# would penalise everyone.
#
# -pipe is likewise left alone; it trades a little memory for not round-tripping through temp
# files, which is the right trade on any machine with RAM to spare.

# tdlib installs headers plus the static archives; nothing here ships at runtime, so the main
# package is empty by design and everything lands in -dev/-staticdev.
ALLOW_EMPTY:${PN} = "1"

# The generator pass above is done in-recipe, so nothing here needs tdlib-native. Left available
# in case something else ever wants it.
BBCLASSEXTEND = "native nativesdk"
