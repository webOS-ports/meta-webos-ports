# Copyright (c) 2024-2026 LG Electronics, Inc.

require webruntime_151.bb
require chromium-host-llvm.inc

PROVIDES = "virtual/webruntime"

PR = "r0"

# Restated here as well as in webruntime_151.bb: this file is reached through

inherit clang_libc

GCC_CROSS_VER = "11.3.0"
DEPEXT = "${@bb.utils.contains('WEBRUNTIME_CLANG_STDLIB', '1', '', '-clang', d)}"

PACKAGECONFIG += "${@bb.utils.contains('USE_WEBRUNTIME_LIBCXX', '1', '', 'system-libcxx', d)}"
PACKAGECONFIG[system-libcxx] = ",,libcxx"

GN_ARGS_CLANG = "is_clang=true"

# Toolchain
# ---------
# Same reasoning as the 120 recipe: build against the wrynose system clang
# (LLVM 22) rather than the clang bundled in third_party/llvm-build, because
# LuneOS links webruntime against the shared system libc++ and compiler and
# standard library have to be the same vintage. clang_use_chrome_plugins goes
# off with it - the plugins are ABI-tied to the bundled clang.
#
# Unlike 120, the flags that upstream clang does not understand are not gated
# locally: meta-browser's 0004-Delete-compiler-options-not-available-in-release-ver
# patch removes them, and that patch is maintained against each Chromium
# release by the layer that cares about OE builds. See webruntime-repo_151.inc.

# compiler-rt
# -----------
# Chromium links libclang_rt.builtins.a explicitly. With clang_base_path
# pointing at the native sysroot, that archive has to be present there for the
# target architecture, and it comes from compiler-rt rather than from clang
# itself. libcxx-native is needed by the native tooling built during the
# Chromium build (the host-side binaries), not by the target.
DEPENDS:append = " compiler-rt compiler-rt-native libcxx-native"

# Chromium looks for the runtime under lib/clang/${clang_version}. Pinning
# clang_version to "latest" and materialising that directory decouples the
# recipe from whichever LLVM version the distro happens to ship, which is the
# whole point of meta-browser doing it this way.

# do_add_clang_latest and the clang_* GN args live in chromium-host-llvm.inc,
# shared with mksnapshot-cross_151.bb.


# Rust
# ----
# Rust is not optional in M151: //url depends on //url:url_rust, so the build
# will not configure without a working Rust toolchain. Chromium's bundled
# rustc (third_party/rust-toolchain) exists in the tarball, but point at the OE
# one so it matches the rest of the build.
#
# rustc_version's value is arbitrary - it exists only to force a rebuild of all
# Rust code when the toolchain changes - but it has to be set or GN errors out.
# libstd-rs is the Rust standard library built for the TARGET; without it
# nothing provides rustlib/${RUST_TARGET_SYS} and do_compile dies in
# find_std_rlibs.py with
#   FileNotFoundError: '.../recipe-sysroot-native/usr/lib/rustlib/x86_64-webos-linux-gnu/lib'
# because rust-native only ships the host triple (x86_64-unknown-linux-gnu).
DEPENDS += "rust-native libstd-rs"
GN_ARGS += " \
    rust_sysroot_absolute=\"${STAGING_DIR_NATIVE}/usr\" \
    rustc_version=\"custom\" \
    rust_target_triple_vendor_for_target=\"${TARGET_VENDOR}\" \
    toolchain_supports_rust_thin_lto=false \
"

do_copy_target_rustlibs () {
    # Chromium wants a single Rust sysroot holding both host and target
    # rustlibs, and rust_sysroot_absolute points at the native one. libstd-rs
    # installs into the target sysroot, so bridge it across. Same approach and
    # same reason as meta-browser's do_copy_target_rustlibs in chromium-gn.inc;
    # keep the two in step.
    for d in ${STAGING_LIBDIR}/rustlib/${TARGET_ARCH}*; do
        [ -d "$d" ] || continue
        cp -r "$d" "${STAGING_LIBDIR_NATIVE}/rustlib/"
    done
}
addtask copy_target_rustlibs after do_configure before do_compile

# The 120 recipe carries ten clang-22 fixup patches
# (webruntime-clang/0001-skcms... through 0010-blink-...gperf-33). They are
# deliberately NOT carried here: they fix Chromium 120 code against a clang far
# newer than it was written for, and M151 is contemporary with LLVM 21/22.
# Several are known fixed upstream (the perfetto and blink template-keyword
# cases, the sandbox SYS_SECCOMP one is now covered by meta-browser's
# fix-SYS_SECCOMP-redefinition.patch). If any turn out to still be needed, add
# them back one at a time rather than reinstating the set - each one that is no
# longer required is a patch that will fail to apply on the next uprev.

# Don't use gold even when selected by default with ld-is-gold in DISTRO_FEATURES
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

# tcmalloc build is broken with clang++ and -mthumb
ARM_INSTRUCTION_SET = "arm"
# M151 turns -Wunsafe-buffer-usage on in places and is generally stricter than
# 120. treat_warnings_as_errors is already false in webruntime-common.inc; keep
# the narrowing demotion from the 120 recipe until a build shows it is
# unnecessary, since it costs nothing and covers third_party code we do not
# maintain.
CLANG_CXXFLAGS = "-Wno-error=c++11-narrowing-const-reference"

# clang_extra_cxxflags is appended AFTER each target's own cflags, so anything
# in it overrides per-target settings. TARGET_CC_ARCH carries -march=nehalem,
# which resets the whole ISA baseline and silently demotes targets that ask for
# a higher one. Skia's AVX-512 colour transform is the case that breaks:
#
#   -msse3 -march=x86-64-v4 -march=nehalem -msse4.2
#           ^ skia's ml4 opts   ^ ours, last, wins
#
#   error: always_inline function '_mm512_cvtph_ps' requires target feature
#   'avx512f', but would be inlined into function 'F_from_H'
#
# That code is runtime-dispatched behind a CPU feature check, so building it
# with AVX-512 enabled is intended and does not raise the baseline for anything
# else. Drop only -march from the flags we inject and keep the rest
# (-m64/-mtune/-mfpmath/-msse4.2), which leaves an effective baseline of
# SSE4.2 - what nehalem means in practice - while letting per-target -march
# work again.
#
# NOTE: this changes codegen slightly versus the 120 recipe, which injects
# -march=nehalem unfiltered. 120 never noticed because its Skia had no
# x86-64-v4 target.
WEBRUNTIME_TARGET_CC_ARCH = "${@' '.join(f for f in d.getVar('TARGET_CC_ARCH').split() if not f.startswith('-march='))}"

# Cross builds must be told the target triple explicitly.
# ------------------------------------------------------
# The oe layer's 0001-Drop-GN-compiler-settings-conflicting-with-OE.patch
# deletes Chromium's own "--target=aarch64-linux-gnu" injection, on the grounds
# that OE already feeds arch flags via the compiler cmdline. That holds for
# meta-browser, where CC is OE's cross wrapper and knows its own target. It does
# NOT hold here: clang_toolchain() invokes recipe-sysroot-native/usr/bin/clang++
# directly, so nothing supplies a triple and clang defaults to the build host.
# On qemux86-64 that was invisible because host == target; on aarch64 the first
# target compile dies with
#   clang++: error: unsupported option '-mbranch-protection=' for target
#   'x86_64-unknown-linux-gnu'
# because it got the target's arch flags but the host's triple.
#
# Only inject when actually cross-compiling, so the proven x86-64 build is
# untouched. Both C and C++ need it - extra_cxxflags alone leaves .c files
# compiling for the wrong architecture, which would fail later at link instead.
# The cross target triple is NOT injected from here. It is restored in
# build/config/compiler_cpu_abi.gn (bringup/0006), because bindgen derives its
# clang arguments from the target config and never sees toolchain extra_cflags.
# See that file for the full reasoning.

GN_ARGS += "${@bb.utils.contains('WEBRUNTIME_CLANG_STDLIB', '1', 'clang_use_stdlib=true clang_extra_cxxflags=\\\"${INCLUDE_PATH_STDLIB} ${WEBRUNTIME_TARGET_CC_ARCH} ${CLANG_CXXFLAGS}\\\"', 'clang_use_stdlib=false clang_extra_cxxflags=\\\"${INCLUDE_PATH_LIBCXX} ${WEBRUNTIME_TARGET_CC_ARCH} ${CLANG_CXXFLAGS}\\\"', d)}"

GN_ARGS += "webos_rpath=\"${libdir}/cbe\""

# V8 requires mksnapshot to have the same pointer width as the target, so for a
# 32-bit target build/toolchain/cros builds the snapshot toolchain as 32-bit x86
# (see the current_cpu selection in its BUILD.gn). That toolchain then wants Rust
# std for i686-unknown-linux-gnu, and OE's rust-native only ships std for the
# build host (x86_64) plus libstd-rs for the target -- so find_std_rlibs.py dies:
#     FileNotFoundError: .../rustlib/i686-unknown-linux-gnu/lib
# aarch64 never hits this because its snapshot toolchain is x64, which host Rust
# already covers.
#
# The entire Rust graph in that toolchain is //third_party/rust/temporal_capi and
# its ICU4X/serde dependencies (plus build/rust/allocator, which only exists
# because Rust is in use), all gated behind v8_enable_temporal_support. Turning it
# off removes the need for i686 Rust std outright. enable_rust=false is not an
# alternative: rust_target.gni asserts on it while temporal_capi is still declared.
#
# This has to be a whole-build setting, not just a snapshot-toolchain one --
# "temporal=$v8_enable_temporal_support" is part of V8's build-config list
# (v8/BUILD.gn), so mksnapshot and the runtime V8 must agree or the snapshot is
# rejected. The cost is that 32-bit targets ship without the Temporal JS API.
# The alternative is building a real i686 Rust std in OE.
GN_ARGS += "${@'v8_enable_temporal_support=false' if d.getVar('TARGET_CPU') in ('arm', 'x86', 'mipsel') else ''}"

GN_ARGS += "${@'cc_wrapper=\\\"ccache \\\"' if bb.data.inherits_class('ccache', d) else ''}"

PACKAGECONFIG[umediaserver] = ",,umediaserver${DEPEXT}"
PACKAGECONFIG[gstreamer] = "use_gst_media=true enable_webm_video_codecs=true,use_gst_media=false,g-media-pipeline${DEPEXT}"
PACKAGECONFIG[webos-codec] = "use_webos_codec=true,use_webos_codec=false,media-codec-interface${DEPEXT}"
PACKAGECONFIG[webos-camera] = "use_webos_camera=true,use_webos_camera=false, cambufferlib${DEPEXT}"

# rust-common gives us RUST_TARGET_SYS / RUST_HOST_SYS / RUST_BUILD_SYS, the
# Rust-style triples OE derives from the OE ones.
inherit rust-common

do_configure:prepend() {
    # M151 added an allowlist of Rust target triples and asserts against it in
    # build/config/rust.gni:
    #   `x86_64-webos-linux-gnu` needs to be added to
    #   `//build/rust/known-target-triples.txt`
    # The recipe passes rust_target_triple_vendor_for_target=${TARGET_VENDOR}
    # (-webos), so the triple OE uses is never one of the 45 upstream ships.
    # meta-browser solves it the same way - see the identical block in
    # chromium-gn.inc - so keep the two in step rather than dropping the vendor.
    # LG's //build/toolchain/linux defines four webOS toolchains -
    # clang_webos_arm, clang_webos_x86, clang_webos_arm64, clang_webos_x64 -
    # and GN evaluates rust.gni for every toolchain referenced anywhere in the
    # graph, not just the one being built for. //third_party/breakpad pulls in
    # an arm toolchain, so an x64 build still asserts on
    # armv7-webos-linux-gnueabihf. Add all four, spelled as rust.gni derives
    # them (see its rust_abi_target block: aarch64/i686/x86_64 take -linux-gnu,
    # armv7 takes -linux-gnueabi + hard-float suffix).
    for triple in ${RUST_TARGET_SYS} ${RUST_HOST_SYS} ${RUST_BUILD_SYS} \
                  x86_64${TARGET_VENDOR}-linux-gnu \
                  i686${TARGET_VENDOR}-linux-gnu \
                  aarch64${TARGET_VENDOR}-linux-gnu \
                  armv7${TARGET_VENDOR}-linux-gnueabihf; do
        grep -qxF "$triple" ${S}/src/build/rust/known-target-triples.txt || \
            echo "$triple" >> ${S}/src/build/rust/known-target-triples.txt
    done

    ln -snf umedia_api_clang.pc ${STAGING_DATADIR}/pkgconfig/umedia_api.pc

    [ -f ${STAGING_DATADIR}/pkgconfig/gmp-player-client-clang.pc ] && \
        ln -snf gmp-player-client-clang.pc ${STAGING_DATADIR}/pkgconfig/gmp-player-client.pc
}

# Parallelism for do_compile.
# ---------------------------------------------------------------------------
# webruntime-common.inc feeds PARALLEL_MAKE straight into NINJA_OPTS, and
# local.conf sets "-j 64" globally, so this is the per-recipe override.
#
# The host is a Threadripper 2990WX, 32 cores / 64 threads, 125 GB. It is only
# that roomy when the BlueStacks guest is down AND its hugepage pool has been
# released - qemu reserves 32768 x 2 MB, and that 64 GB stays reserved even
# after the VM exits, showing up as "used" that no process accounts for
# (check HugePages_Total in /proc/meminfo; free it with
# sysctl vm.nr_hugepages=0). With the guest running, drop this to about 24.
#
# concurrent_links stays at 1 regardless: Chromium sizes it from MemTotal at
# 30-50 GB per link, and the libcbe.so lld invocation is the single biggest
# allocation in the build. Running several of those at once is what pushed the
# box to 3 GB free on 2026-08-20.
GN_ARGS += "concurrent_links=1"
PARALLEL_MAKE = "-j 64"
