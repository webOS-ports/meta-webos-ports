# Copyright (c) 2024-2026 LG Electronics, Inc.
#
# Source layout for webruntime 158.
#
# github.com/webos-ports/chromium158 is the upstream 158 tarball with the neva,
# oe, luneos and bringup layers applied, one commit per patch, in the same way
# as chromium151. See webruntime-repo_151.inc for the history of that layout.

CHROMIUM_PV = "158.0.8127.36"

# See webruntime-repo_151.inc: PV comes from WEBOS_VERSION. Bump the submission
# when the repo changes in a way worth telling apart at package level.
WEBOS_VERSION = "${CHROMIUM_PV}-0"

WEBRUNTIME_REPO_VERSION = "158"

# name=chromium is load-bearing, see webruntime-repo_151.inc. lfs=0: the upstream
# tarball carries third_party/litert's prebuilt binaries as Git LFS pointers
# with no objects behind them, and nothing here uses them.
SRC_URI = "git://github.com/webos-ports/chromium158.git;protocol=https;branch=main;name=chromium;lfs=0"
SRCREV = "8358ff267687aef0ce1c7910cded1337f5c79d6b"

# The prebuilt toolchains below are too big for GitHub (100 MiB per file), so
# they stay out of the repo and come from the release tarball of the same
# version. unpack=0: only these directories are taken from it, by
# unpack_chromium_toolchains.
SRC_URI += "https://commondatastorage.googleapis.com/chromium-browser-official/chromium-${CHROMIUM_PV}.tar.xz;name=chromium-tarball;unpack=0"
SRC_URI[chromium-tarball.sha256sum] = "000e7ce96ca59f48e278a652189e816c9c9686b80479d13693c2957e349b0cec"

ERROR_QA:remove = "webos-enh-sub-warning"
WARN_QA:remove = "webos-enh-sub-warning"

CHROMIUM_LICENSE_CHKSUM = "file://src/LICENSE;md5=c408a301e3407c3803499ce9290515d6"
CHROMIUM_OSS_PKG_INFO_CHKSUM = ""

# Rust
# ----
# 158 is built with the toolchain Chromium pins (rustc, bindgen and Crubit from
# third_party/rust-toolchain, the standard library from third_party/rust-src).
# Crubit generates C++ bindings against that exact compiler, so OE's rust-native
# cannot stand in for it. node comes from the same tarball.
#
# All of these are x86-64 Linux binaries, as are the gn, gperf and esbuild
# prebuilts in the repo, so this recipe cannot build on any other host.
python () {
    if d.getVar('BUILD_ARCH') != 'x86_64':
        raise bb.parse.SkipRecipe("webruntime 158 uses Chromium's x86-64 prebuilt toolchains")
}

do_unpack[depends] += "xz-native:do_populate_sysroot"
do_unpack[postfuncs] += "unpack_chromium_toolchains"
unpack_chromium_toolchains() {
    top=chromium-${CHROMIUM_PV}
    tar -xJf ${DL_DIR}/$top.tar.xz -C ${S}/src --strip-components=1 \
        $top/third_party/rust-toolchain \
        $top/third_party/rust-src \
        $top/third_party/node/linux
}

# gn gen asks the host pkg-config for dbus-1, the glib-2.0 family and nss.
DEPENDS += "ninja-native dbus-native glib-2.0-native nss-native"

# Chromium links with its CIPD mold wherever use_mold defaults on, see
# webruntime-repo_151.inc. :append so that it survives the plain assignment of
# GN_ARGS in webruntime-common.inc.
GN_ARGS:append = " use_mold=false"

# libyuv's SME code calls __arm_get_current_vg, which neither libgcc nor the
# compiler-rt builtins in the OE sysroot provide, so libcbe.so does not link.
GN_ARGS:append:aarch64 = " libyuv_use_sme=false"

# V8's metagen parses its headers with libclang. The llvm-libclang package in
# the tarball is LLVM 24 and rejects the LLVM 22 builtin headers of OE's clang
# on arm ("static declaration of '__arm_set_fpm_lscale' follows non-static"),
# so use the libclang and Python bindings that clang-native provides.
inherit python3-dir
GN_ARGS:append = " v8_metagen_libclang_so=\"${STAGING_DIR_NATIVE}/usr/lib/libclang.so\""
GN_ARGS:append = " v8_metagen_libclang_bindings_dir=\"${STAGING_DIR_NATIVE}/usr/lib/${PYTHON_DIR}/site-packages\""

do_configure:prepend() {
    # webruntime-common.inc calls ${S}/depot_tools/ninja. gn stays the repo's
    # prebuilt: 158 needs a newer gn than gn-native, which is pinned for 151.
    ln -sfn ${STAGING_BINDIR_NATIVE}/ninja ${S}/depot_tools/ninja
}

# A device whose GPU driver can only map buffers below 4GB (Adreno 4xx on a
# 3.10 kgsl) opts out of the low 4GB reservation with
# /etc/webruntime/no-low-4gb-reservation; the machine layer installs it.
SRC_URI:append:halium = " \
    file://158/0001-Halium-let-a-device-opt-out-of-the-low-4GB-reservation.patch \
"
