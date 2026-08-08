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
SRCREV = "6984a0a27f65bf49be1ad829a85a02b1494af17a"

S = "${WORKDIR}/git/messaging/telegram/plugin/tdlib-src"

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

# TDLib generates a very large set of C++ sources and then compiles them, and the generated
# translation units are individually huge. On a builder with limited RAM per core this is the
# recipe that will OOM first; upstream's own advice is to split the generated sources
# (SplitSource.php) or to cap parallelism. Capping is the simpler lever and is why it is here
# rather than left to the default.
PARALLEL_MAKE = "-j 2"

# The generated sources push compile memory well past what -pipe assumes.
CXXFLAGS:remove = "-pipe"

# tdlib installs headers plus the static archives; nothing here ships at runtime, so the main
# package is empty by design and everything lands in -dev/-staticdev.
ALLOW_EMPTY:${PN} = "1"

# The generator pass above is done in-recipe, so nothing here needs tdlib-native. Left available
# in case something else ever wants it.
BBCLASSEXTEND = "native nativesdk"
