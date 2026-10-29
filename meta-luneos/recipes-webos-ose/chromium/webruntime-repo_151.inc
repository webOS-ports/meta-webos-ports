# Copyright (c) 2024-2026 LG Electronics, Inc.
#
# Source layout for webruntime 151.
#
# The retired 108 and 120 recipes fetched a ready-made LG tree from the webOS
# OSE git server, where //neva, //webos and //ozone were already part of the
# checkout. There is no LG drop for 151, so this one is assembled instead:
# pristine Chromium from the upstream tarball, then three patch layers applied
# in order.
#
#   1. neva      the LG port itself (//neva, //webos, //ozone plus the
#                modifications those need in upstream subsystems). Generated
#                from the 120 -> 151 forward port; see the README committed in
#                the chromium151 repo (a97ebbdf50).
#   2. oe        OpenEmbedded build fixes, taken from meta-browser so they stay
#                in sync with the layer that maintains them.
#   3. luneos    the LuneOS patches, unchanged in intent from the 120 series.
#                These used to live in files/ as 0001..0012; they are commits
#                in the repo now, so files/ no longer carries them.
#
# Keeping the three apart is the point: when Chromium 152 lands, each layer can
# be rebased on its own and it stays obvious which changes are LG's, which are
# OE's and which are ours.

CHROMIUM_PV = "151.0.7922.108"

# webruntime-common.inc inherits webos_enhanced_submissions, which derives PV
# from WEBOS_VERSION:
#     <component-version>-<submission>[_<40-char-revision-hash>]
# Without it PV comes out as "0" and the recipe is invisible to
# PREFERRED_VERSION_webruntime - bitbake silently keeps building 120.
#
# 108 and 120 put the LG git SHA in the hash field. There is no LG drop for
# 151, so only the version and submission are given. The submission is "0"
# because this is the first assembly of the tree, not an LG re-spin; bump it
# when the patch layers change in a way worth distinguishing at package level.
WEBOS_VERSION = "${CHROMIUM_PV}-0"

WEBRUNTIME_REPO_VERSION = "151"

# Not fetched from the webOS OSE git server, so the repo-name machinery in
# webruntime-common.inc does not apply. SRC_URI is assigned, not appended.
# Sources come from the pre-patched repo, not the upstream tarball.
# ---------------------------------------------------------------------------
# github.com/webos-ports/chromium151 is the upstream tarball with the four
# layers already applied, one commit per patch:
#
#   3 commits  chromium 151.0.7922.108 pristine upstream (split only because
#              GitHub refuses any single push over 2 GiB)
#  15 commits  neva      - the LG port
#  15 commits  oe        - OpenEmbedded build fixes
#  13 commits  luneos    - LuneOS specifics
#   6 commits  bringup   - the M151 API migration
#
# This replaces do_unpack (152 s) + do_patch (1043 s) with a git fetch that OE
# caches in DL_DIR. do_patch cost that much because PATCHTOOL = "git" ran
# git init + git add -A over the 27 GB tree for every build, not because
# applying 49 patches is slow.
#
# The patches are still generated for review by
# chromium-port/tools/gen_bringup_patches.sh; they are simply no longer the
# thing the build consumes. To move a fix into the build, commit it to the repo
# and bump SRCREV.
#
# Excluded from the repo - prebuilt host toolchains and test corpora that
# GitHub's 100 MiB per-file hard limit rules out (rust-toolchain alone holds a
# 262 MB archive, a 236 MB librustc_driver.so and a 104 MB libclang.so):
#   third_party/{llvm,llvm-build,llvm-build-tools,rust-src,instrumented_libs,
#   jetstream,speedometer}, chrome/build/pgo_profiles and
#   third_party/openscreen/src/third_party/llvm-build are absent from the
#     generated ninja graph, so nothing has to replace them;
#   third_party/node/linux and third_party/rust-toolchain ARE referenced
#     (2273 and 183 edges), so do_configure:prepend below shims them onto OE's
#     tools. Re-check this with a grep over build/Release/**/*.ninja whenever
#     the exclusion list changes - do_unpack succeeding proves nothing here.
# name=chromium is load-bearing: webruntime-common.inc inherits
# webos_enhanced_submissions, which only stands down for a git SRC_URI whose
# name is absent or "main". Without it the class tries to treat this as an LG
# enhanced submission and emits webos-enh-sub-error QA failures ("Branch is set
# in WEBOS_VERSION", "Revision ... defined in WEBOS_VERSION", "Unable to update
# checkout"), which are non-fatal per task but still make bitbake exit non-zero.
# With the name present it emits a single webos-enh-sub-warning instead, which
# ERROR_QA:remove below drops - same approach as mksnapshot-cross_151.bb.
SRC_URI = "git://github.com/webos-ports/chromium151.git;protocol=https;branch=main;name=chromium"
SRCREV = "0baacb2b1a4b885826fed5493262f759c6a5b469"

ERROR_QA:remove = "webos-enh-sub-warning"
WARN_QA:remove = "webos-enh-sub-warning"

# No S assignment: bitbake.conf's default S = "${UNPACKDIR}/${BP}" matches the
# git fetcher's BB_GIT_DEFAULT_DESTSUFFIX = "${BP}", and oe-core now errors out
# on recipes that still set S = "${UNPACKDIR}/git" by hand.
#
# The repo already carries src/ at its root and the depot_tools/{gn,ninja}
# symlinks that webruntime-common.inc expects on PATH, so the tarball-era
# do_unpack:append relocation and shim creation are gone.

CHROMIUM_LICENSE_CHKSUM = "file://src/LICENSE;md5=c408a301e3407c3803499ce9290515d6"
CHROMIUM_OSS_PKG_INFO_CHKSUM = ""

# Shims for the two excluded paths the build actually needs
# ---------------------------------------------------------------------------
# Both are host tool directories that are far too big for GitHub (node 115.5 MB;
# rust-toolchain holds a 262 MB archive, a 236 MB librustc_driver.so and a
# 104 MB libclang.so, against a 100 MiB per-file hard limit), so they stay out
# of the repo and OE supplies the tools instead. The paths are hardcoded in
# Chromium and not settable by GN args, so the shims have to live in the tree.

# Chromium 151 pins node v24 (third_party/node/update_node_binaries) and
# check_version.js compares the major version, so meta-oe's 22.23.1 is rejected.
# nodejs24-native is meta-oe master's 24.19.0 renamed, so it does not displace
# nodejs-native 22 for the Enact/enyo tooling - see
# meta-luneos/recipes-devtools/nodejs24.
DEPENDS += "nodejs24-native bindgen-cli-native"

do_configure:prepend() {
    # node: the graph calls third_party/node/node.py 3990 times and node.py
    # hardcodes this path with no environment override, so point it at the
    # nodejs24-native binary.
    install -d ${S}/src/third_party/node/linux/node-linux-x64/bin
    ln -sfn ${STAGING_BINDIR_NATIVE}/node \
        ${S}/src/third_party/node/linux/node-linux-x64/bin/node

    # rust: 183 ninja edges name //third_party/rust-toolchain even though
    # rust_sysroot_absolute already points at OE. rustc_wrapper_inputs in
    # build/config/rust.gni appends bin/rustc unconditionally, and the
    # clang_x64_for_rust_host_build_tools sub-toolchain sets
    # toolchain_for_rust_host_build_tools, which forces
    # use_chromium_rust_toolchain back on regardless of our GN args.
    install -d ${S}/src/third_party/rust-toolchain/bin
    ln -sfn ${STAGING_BINDIR_NATIVE}/rustc ${S}/src/third_party/rust-toolchain/bin/rustc
    ln -sfn ${STAGING_BINDIR_NATIVE}/bindgen ${S}/src/third_party/rust-toolchain/bin/bindgen
    # rust_bindgen_generator.gni derives libclang as ${rust_bindgen_root}/lib.
    ln -sfn ${STAGING_LIBDIR_NATIVE} ${S}/src/third_party/rust-toolchain/lib

    # rustfmt is a required input and run_bindgen.py check_call()s it, but
    # oe-core installs it only for class-nativesdk, not class-native. It is
    # purely cosmetic here: run_bindgen.py already passes --no-rustfmt-bindings
    # to bindgen, then reformats the result as a separate step, so skipping it
    # leaves valid unformatted Rust. Drop this shim if rust-native ever ships
    # rustfmt.
    printf '#!/bin/sh\nexit 0\n' > ${S}/src/third_party/rust-toolchain/bin/rustfmt
    chmod +x ${S}/src/third_party/rust-toolchain/bin/rustfmt
}

# Halium specific patch for DRI
SRC_URI:append:halium = " \
    file://0001-Halium-Disable-DRI-build.patch \
"

# A device whose GPU driver can only map buffers below 4GB (Adreno 4xx on a
# 3.10 kgsl) opts out of the low 4GB reservation with
# /etc/webruntime/no-low-4gb-reservation; the machine layer installs it.
SRC_URI:append:halium = " \
    file://0002-Halium-let-a-device-opt-out-of-the-low-4GB-reservation.patch \
"

# Non-x86_64 (aarch64) build hosts
# ---------------------------------------------------------------------------
# The repo carries several host tools only as x86_64 binaries. On an aarch64
# build host each one fails with "Exec format error" (the rollup addon with
# "Cannot find module @rollup/rollup-linux-arm64-gnu"), each at a different
# point in the build:
#
#   x86_64 binary in the tree                       first run in   replaced by
#   src/buildtools/linux64/gn (CIPD linux-amd64)    do_configure   gn-webruntime-native
#   src/third_party/ninja/ninja                     do_compile     ninja-native
#   src/third_party/gperf/cipd/bin/gperf (Blink)    do_compile     gperf-native
#   src/buildtools/third_party/mold/cipd/mold       first link     lld (use_mold=false)
#   devtools-frontend third_party/esbuild/esbuild   do_compile     @esbuild/linux-arm64
#   devtools-frontend @rollup/rollup-linux-x64-gnu  do_compile     @rollup/rollup-linux-arm64-gnu
#
# depot_tools/{gn,ninja} are symlinks to the first two, so replacing the
# targets covers both. use_mold defaults to true for a non-official clang
# Linux build (build/config/compiler/compiler.gni) and points --ld-path at
# the CIPD mold; lld-native is already a dependency, so fall back to lld.
# The devtools pair is reached from //third_party/blink/public through
# devtools_inspector_resources_grit -> inspector_overlay: its ts_library runs
# esbuild (devtools_skip_typecheck is on for non-official builds) and
# build_inspector_overlay runs rollup 4, whose native.js loads the addon by
# platform name. esbuild's JS API rejects a binary whose version differs from
# node_modules/esbuild, so the two versions below must match the tree:
# esbuild 0.25.1, rollup 4.60.4. Re-check both whenever SRCREV moves. Both
# tarballs were checked against the npm registry's dist.integrity.
#
# An x86_64 host keeps using every binary exactly as shipped.
DEPENDS += "${@'' if d.getVar('BUILD_ARCH') == 'x86_64' else 'gn-webruntime-native ninja-native'}"
GN_ARGS += "${@'' if d.getVar('BUILD_ARCH') == 'x86_64' else 'use_mold=false'}"

# 0003: build/toolchain/cros pins the V8 snapshot toolchain to x64 for every
# 64-bit target, which on an aarch64 host builds mksnapshot/torque as x86_64
# binaries it cannot run. See the patch.
WEBRUNTIME_AARCH64_HOST_SRC_URI = " \
    https://registry.npmjs.org/@esbuild/linux-arm64/-/linux-arm64-0.25.1.tgz;name=esbuild-arm64;subdir=esbuild-arm64;downloadfilename=esbuild-linux-arm64-0.25.1.tgz \
    https://registry.npmjs.org/@rollup/rollup-linux-arm64-gnu/-/rollup-linux-arm64-gnu-4.60.4.tgz;name=rollup-arm64;subdir=rollup-arm64;downloadfilename=rollup-linux-arm64-gnu-4.60.4.tgz \
    file://0003-aarch64-host-build-the-V8-snapshot-tools-for-the-host.patch \
"
SRC_URI:append = "${@' ${WEBRUNTIME_AARCH64_HOST_SRC_URI}' if d.getVar('BUILD_ARCH') == 'aarch64' else ''}"
SRC_URI[esbuild-arm64.sha256sum] = "70771c9212585cfd1b190465f92dae98d1d3fc4a4fab5cacbef71457ee08e254"
SRC_URI[rollup-arm64.sha256sum] = "7b98541552d3d679d4a92a04d590b34d1b44a6ec582483be9a66e8bdc31a06f6"

webruntime_replace_x86_host_tools() {
    cd ${S}/src
    ln -sfn ${STAGING_BINDIR_NATIVE}/gn-webruntime buildtools/linux64/gn
    ln -sfn ${STAGING_BINDIR_NATIVE}/ninja third_party/ninja/ninja
    ln -sfn ${STAGING_BINDIR_NATIVE}/gperf third_party/gperf/cipd/bin/gperf

    if [ "${BUILD_ARCH}" = "aarch64" ]; then
        devtools=third_party/devtools-frontend/src
        install -m 0755 ${UNPACKDIR}/esbuild-arm64/package/bin/esbuild \
            $devtools/third_party/esbuild/esbuild
        rm -rf $devtools/node_modules/@rollup/rollup-linux-arm64-gnu
        cp -R ${UNPACKDIR}/rollup-arm64/package \
            $devtools/node_modules/@rollup/rollup-linux-arm64-gnu
    fi
}
do_configure[prefuncs] += "${@'' if d.getVar('BUILD_ARCH') == 'x86_64' else 'webruntime_replace_x86_host_tools'}"
