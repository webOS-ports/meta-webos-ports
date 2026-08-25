# Copyright (c) 2024-2025 LG Electronics, Inc.

# M151: the Enact app class (webos_enactjs_app.bbclass) builds each app's
# snapshot_blob.bin with mksnapshot-cross. A snapshot is only loadable by the
# V8 that produced it, so building M151 apps with the M120 mksnapshot yields
# blobs the runtime rejects and the app fails to start. That is what pinning
# mksnapshot-cross to 120.% for qemux86-64 was doing.

inherit cross
inherit pkgconfig

WEBRUNTIME_REPO_VERSION = "151"
require webruntime-common.inc
require webruntime-repo${REPO_VERSION}.inc

# Intentionaly disable uninative it failed to execute x86 binary on x64 host
# 32bit executable mksnapshot is required to create snapshot for the 32bit target
UNINATIVE_LOADER = ""

# M114: To avoid error "--dynamic-linker=: must take a non-empty argument" | [http://repo.lge.com:8080/c/neva/meta-lg-webos-neva/+/9221]
# append is added to work-around mismatch of basebash
BUILD_LDFLAGS:append = " -Wl,--allow-shlib-undefined -Wl,--dynamic-linker="
BUILD_LDFLAGS:remove = " -Wl,--allow-shlib-undefined -Wl,--dynamic-linker="

# Same as webruntime_151: this builds from the upstream chromium tarball, not a
# git submission, so webos_enhanced_submission (pulled in via webruntime-common)
# has nothing to check and its warning would otherwise fail the build.
ERROR_QA:remove = "webos-enh-sub-warning buildpaths"

PROVIDES = "mksnapshot-cross-${TARGET_ARCH}"
PN = "mksnapshot-cross-${TARGET_ARCH}"
BPN = "mksnapshot"
PR = "r0"

TARGET = "v8_snapshot_clang_${TARGET_CPU}/mksnapshot"

DEPENDS = "glib-2.0-native gcc-runtime"

# AFTER the DEPENDS assignment above, deliberately: that line is a plain "="
# that resets the list, so anything a require adds before it is silently lost.
require chromium-host-llvm.inc

# M151 loads //crypto while generating the v8 snapshot toolchain, and
# crypto/BUILD.gn adds the //build/config/linux/nss pkg_config when
# use_nss_certs is set - it defaults to true on linux (crypto/features.gni).
# That makes gn gen shell out to pkg-config for nss, which is not in this
# recipe's sysroot: webruntime.inc DEPENDS on nss/nspr, but this recipe defines
# its own DEPENDS and only builds a host-side v8 snapshot tool. mksnapshot has
# no use for the platform certificate library, so switch the feature off rather
# than build target nss for it.
# Point Rust at OE's toolchain, the same way webruntime-clang_151.bb does.
#
# build/config/rust.gni does
#     rustc_revision = read_file("//third_party/rust-toolchain/VERSION", ...)
# whenever use_chromium_rust_toolchain is on, and that is just
# "rust_sysroot_absolute == \"\"", so it defaults on. This recipe never set any
# rust args and got away with it only while the sources came from the Chromium
# tarball, which does ship third_party/rust-toolchain. The git repo excludes it
# (a prebuilt toolchain we do not use), so gn gen now stops with
#     ERROR at //build/config/rust.gni:174:21: Could not read file.
#
# enable_rust=false is NOT an option: M151's V8 pulls in //third_party/rust/
# temporal_capi for the Temporal proposal, and rust_target.gni asserts
# enable_rust. Only rust_sysroot_absolute is needed here, not the
# rust_target_triple_vendor_for_target/libstd-rs pair webruntime uses: the v8
# snapshot toolchain builds mksnapshot to run on the build host, so its Rust is
# host Rust (x86_64-unknown-linux-gnu) and rust-native already provides it.
DEPENDS += "rust-native"

# Same as webruntime-clang_151.bb: rust-common pulls in rust-target-config,
# which exports RUST_TARGET_PATH and adds do_rust_gen_targets. The oe layer's
# 0009-Adjust-the-Rust-build patch makes build/rust/std/find_std_rlibs.py look
# up ${RUST_TARGET_PATH}/<target>.json unconditionally - even for a builtin
# triple like x86_64-unknown-linux-gnu - so without this it dies with
#   KeyError: 'RUST_TARGET_PATH'
inherit rust-common

# webruntime-repo_151.inc adds these and creates the matching shims under
# third_party/{node,rust-toolchain} in do_configure:prepend, but line 37 above
# is a plain "DEPENDS =" assignment that runs after the require and wipes them.
# The shims are still created, so they end up as dangling symlinks and ninja
# reports the tool "missing and no known rule to make it".
DEPENDS += "bindgen-cli-native nodejs24-native"

# Same reason as the rust args: without clang_base_path, //build/config/compiler
# reads //third_party/llvm-build/Release+Asserts/cr_build_revision to identify
# Chromium's bundled clang. That is 874 MB of prebuilt toolchain, excluded from
# the git repo because we compile with OE's clang - but it does ship in the
# tarball, which is why this recipe only started failing after the switch.
# webruntime-clang_151.bb sets these; this recipe requires webruntime-common.inc
# but not that .bb, so it needs its own copy.

# See webruntime-clang_151.bb for the full reasoning: a 32-bit target makes the
# v8 snapshot toolchain 32-bit x86, which wants Rust std for
# i686-unknown-linux-gnu that OE does not provide. Dropping Temporal removes the
# only Rust in that toolchain. It must match webruntime-clang_151.bb exactly --
# "temporal=..." is in V8's build-config list, so a mksnapshot that disagrees
# with the runtime V8 produces a snapshot the runtime refuses.
GN_ARGS += "${@'v8_enable_temporal_support=false' if d.getVar('TARGET_CPU') in ('arm', 'x86', 'mipsel') else ''}"

GN_ARGS:append = "\
    use_pmlog=false \
    use_nss_certs=false \
    icu_use_data_file=false \
    rust_sysroot_absolute=\"${STAGING_DIR_NATIVE}/usr\" \
    rustc_version=\"custom\" \
    toolchain_supports_rust_thin_lto=false \
"

# icu_use_data_file=false embeds the ICU data in the binary. It defaults to
# true in third_party/icu/config.gni, which makes mksnapshot look for an
# icudtl.dat next to itself at runtime; only the binary is installed, so
# com.webos.app.test.v8snapshot fails with
#   mksnapshot-cross-aarch64: Failed to initialize ICU
# The 120 recipe installs just the binary too and never needed this, so M151
# changed the default behaviour here. Embedding keeps this a self-contained
# cross tool rather than adding a data file to ${bindir} and hoping the
# working directory lines up.

# This is a full V8 build (~3700 ninja targets), not a small helper, so give it
# the same treatment as webruntime-clang. Without this it falls back to the
# global PARALLEL_MAKE and compiled V8 at -j 16 on a 64-thread box, leaving it
# the long pole of the qemux86-64 appliance build with two thirds of the CPU
# idle. See the parallelism note in webruntime-clang_151.bb for the memory
# reasoning; V8 alone is far lighter than Chromium's link so this is safe.
PARALLEL_MAKE = "-j 48"

EXTRA_OEGN = "--root=${S}/src --dotfile=mksnapshot.gn"

do_install() {
    echo "Installing ${PN}"
    install -d ${D}${bindir}
    install ${OUT_DIR}/v8_snapshot_clang_${TARGET_CPU}/mksnapshot ${D}${bindir}/mksnapshot-cross-${TARGET_ARCH}
}
