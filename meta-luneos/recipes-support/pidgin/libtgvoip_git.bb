SUMMARY = "VoIP library used by Telegram clients"
DESCRIPTION = "libtgvoip, the voice engine tdlib-purple links for Telegram calling. Built from the \
monorepo's copy rather than upstream: it carries webOS-specific work in VoIPController.cpp, and it \
is the tree tdlib-purple's call.cpp is written against."
HOMEPAGE = "https://github.com/telegramdesktop/libtgvoip"
SECTION = "libs"
LICENSE = "Unlicense"
LIC_FILES_CHKSUM = "file://UNLICENSE;md5=1d267ceb3a8d8f75f1be3011ee4cbf53"

# ALSA rather than PulseAudio, matching --without-pulse below. openssl is for the call crypto,
# libopus for the codec; both are linked by tdlib-purple too, because this builds static.
DEPENDS = "libopus alsa-lib openssl"

# Pinned with the plugin recipes -- see purple-synergy.inc. Not required here (this is a library,
# not a prpl, so none of that file's purple-2 packaging applies), but kept on the same SRCREV so a
# bump moves the engine and the plugin together.
SRC_URI = "git://github.com/Herrie82/webos-synergy-revival.git;branch=herrie/telegram-tdlib-1.8.66;protocol=https;destsuffix=git"
SRCREV = "23e8193c5ff9c83a7a9f275af2947d868d3d2ce2"

PV = "1.0+git"

S = "${WORKDIR}/git/messaging/telegram/plugin/libtgvoip"

inherit autotools pkgconfig

# --with-pic is not redundant next to --disable-shared, it is required BY it. libtool only compiles
# position-independent objects when it is building a shared library; with shared output disabled it
# produces non-PIC objects, and the archive is then unusable in the Telegram plugin's .so:
#
#   libtgvoip.a(VoIPController.o): relocation R_X86_64_32S against symbol `AES_encrypt@@OPENSSL_3.0.0'
#   can not be used when making a shared object; recompile with -fPIC
#
# --with-pic tells libtool to emit PIC objects regardless of what it is linking them into.
# build-libtgvoip.sh gets there by a different route: it puts -fPIC directly in its ARCH flags.
#
# Static only otherwise, which is how tdlib-purple consumes it on the device too. A shared
# libtgvoip would also work, but keeping the two builds alike means a linkage problem here is a
# real problem and not a configuration difference.
EXTRA_OECONF = "--without-pulse --with-alsa --enable-static --disable-shared --with-pic"

# libtool: "unable to infer tagged configuration" / "specify a tag with '--tag'" when linking
# libtgvoip.la.
#
# Nothing to do with cross-compiling. Makefile.am lists the Darwin backends (os/darwin/*.mm,
# webrtc_dsp/rtc_base/logging_mac.mm) in libtgvoip_la_SOURCES, and automake picks the link language
# from the whole source list regardless of what actually compiles on this host. Objective-C++ wins,
# so the library is linked with $(OBJCXXLINK) -- which automake emits WITHOUT a --tag, because
# libtool has no Objective-C++ tag to offer.
#
# Lacking a tag, libtool falls back to inferring one, and that is what fails here. The generated
# libtool has available_tags='CXX ' and records that tag's compiler as the FULL OE command string:
#
#     CC="x86_64-webos-linux-g++  -m64 -march=nehalem ... --sysroot=.../recipe-sysroot"
#
# Inference works by matching the link command against that string. OBJCXXLINK invokes $(OBJCXXLD)
# with $(OBJCXXFLAGS), and OE populates CXXFLAGS but not OBJCXXFLAGS, so the command is a bare
# `x86_64-webos-linux-g++ -Wl,...` that matches nothing and inference gives up.
#
# This is also why the device build never hits it -- not regeneration. build-libtgvoip.sh leaves
# CXX as the bare `arm-unknown-linux-gnueabi-g++` and passes flags separately, so the recorded
# string is just the compiler name and inference succeeds. (The tracked Makefile.in has the same
# tagless OBJCXXLINK, so autoreconf changes nothing here.)
#
# $(CXXLINK), which automake generates a few lines earlier in the same Makefile, is the identical
# command but passes --tag=CXX explicitly, so no inference is needed at all. Point one at the other
# rather than open-coding the command, so it stays correct if automake changes it.
EXTRA_OEMAKE = "OBJCXXLINK='$(CXXLINK)'"

# DSP (echo cancellation, noise suppression, AGC) is left at its default of enabled. It matters
# that this stays in step with tdlib-purple: EchoCanceller.h gates its webrtc:: members on
# TGVOIP_NO_DSP, so a library built one way and a plugin compiled the other way disagree about the
# layout of that class. tdlib-purple_git.bb therefore does NOT pass -Dtgvoip_NO_DSP. Change both
# or neither.

# Headers land in ${includedir}/tgvoip (Makefile.am: tgvoipincludedir). Nothing ships at runtime
# because the only output is a static archive, so the main package is empty by design.
ALLOW_EMPTY:${PN} = "1"
