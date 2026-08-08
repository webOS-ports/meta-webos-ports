SUMMARY = "Telegram protocol plug-in for libpurple (TDLib)"
DESCRIPTION = "libpurple prpl for Telegram built on TDLib, with webOS voice calling via an \
in-tree libtgvoip."
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=60e1c5c15dfde4c879ef9a5f0f20feab"

require purple-synergy.inc

# tdlib comes from tdlib_1.8.66.bb alongside this recipe. It is the expensive part of a Telegram
# build: TDLib compiles a very large set of generated C++ sources and will be the first thing to
# OOM on a constrained builder, which is why that recipe caps PARALLEL_MAKE.
DEPENDS = "pidgin glib-2.0 zlib openssl tdlib"

S = "${WORKDIR}/git/messaging/telegram/plugin/tdlib-purple"

inherit cmake pkgconfig

# tdlib-purple declares cmake_minimum_required(VERSION 3.2). Scarthgap's CMake still accepts that
# with a deprecation warning; CMake 4 does not, and will need
# -DCMAKE_POLICY_VERSION_MINIMUM=3.5 added to EXTRA_OECMAKE when the host cmake is bumped.

# NoVoip=OFF keeps the in-tree libtgvoip, which is what carries webOS calling. NoLottie=ON drops
# the rlottie sticker renderer: it is a large dependency and the webOS UI does not animate them.
EXTRA_OECMAKE = " \
    -DNoVoip=OFF \
    -DNoLottie=ON \
    -DNoTests=ON \
"

# The webOS calling bridge (call-luna.cpp, com.palm.telegram.call) needs luna-service2, and
# messaging/common/webos-ls2-compat.h to map the legacy split-bus API onto 3.21.2's single bus.
# Add "luna-service2" to DEPENDS and -I${WORKDIR}/git/messaging/common to the compile flags when
# turning it on. Video additionally needs a voipkit backend that does not exist for LuneOS
# (libpalmgstskype is legacy webOS only) -- voipkit_none.c is the null one.
