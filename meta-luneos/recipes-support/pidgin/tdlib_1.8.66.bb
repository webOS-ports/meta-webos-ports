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

BBCLASSEXTEND = "native nativesdk"
