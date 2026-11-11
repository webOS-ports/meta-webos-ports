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
# main, which has pull requests #20 (browser-shell video state) and #21 (the
# Widevine bridge's media changes).
SRC_URI = "git://github.com/webos-ports/chromium151.git;protocol=https;branch=main;name=chromium"
SRCREV = "6384e27a9746d086ba69bf6cc9bf47f20ddbf1ec"

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
DEPENDS += "nodejs24-native bindgen-cli-native gn-native ninja-native gperf-native esbuild-native"

# devtools-frontend's rollup 4.60.4 loads a native addon for the build host,
# and node_modules carries only @rollup/rollup-linux-x64-gnu. The wasm-node
# package of the same version has the identical rollup JavaScript and loads
# WebAssembly bindings instead; do_configure:prepend puts its loader in.
SRC_URI += "https://registry.npmjs.org/@rollup/wasm-node/-/wasm-node-4.60.4.tgz;name=rollup-wasm-node;downloadfilename=rollup-wasm-node-4.60.4.tgz;subdir=rollup-wasm-node"
SRC_URI[rollup-wasm-node.sha256sum] = "a70354216d534b0b7cba485954570eea539389459bdf1a4cf02c9514607c74e6"

# Chromium links with its CIPD mold (buildtools/third_party/mold/cipd/mold),
# an x86-64 prebuilt, wherever use_mold defaults on: a clang build for an x64
# or arm64 linux toolchain that is not official (build/config/compiler/
# compiler.gni). In the graph for halium-arm64 that is 62 links - the rust host
# build scripts, clang_x64 host tools, v8_snapshot_clang_arm64 and ANGLE's
# libEGL/libGLESv2/libvk_swiftshader. Turn it off so they link with OE's lld
# like every other link in the build. :append so that it survives any later
# plain assignment of GN_ARGS (webruntime-common.inc sets it with "=").
GN_ARGS:append = " use_mold=false"

do_configure:prepend() {
    # gn and ninja: the repo's depot_tools/{gn,ninja} point at the x86-64
    # prebuilts in src/buildtools/linux64/gn and src/third_party/ninja/ninja,
    # which do not run on any other build host ("Exec format error" on an
    # aarch64 builder). webruntime-common.inc puts depot_tools first on PATH
    # and calls ${S}/depot_tools/ninja directly, so repoint the two links at
    # OE's. gn-native is pinned to Chromium's gn_version by
    # recipes-devtools/gn/gn_git.bbappend.
    ln -sfn ${STAGING_BINDIR_NATIVE}/gn ${S}/depot_tools/gn
    ln -sfn ${STAGING_BINDIR_NATIVE}/ninja ${S}/depot_tools/ninja

    # gperf: third_party/blink/renderer/build/scripts/scripts.gni hardcodes
    # //third_party/gperf/cipd/bin/gperf on a linux host, an x86-64 prebuilt
    # (12 Blink generator edges), so point it at gperf-native.
    ln -sfn ${STAGING_BINDIR_NATIVE}/gperf ${S}/src/third_party/gperf/cipd/bin/gperf

    # esbuild: devtools-frontend's ts_library.py and esbuild.js both run
    # third_party/esbuild/esbuild (devtools_paths.esbuild_path(), and
    # ESBUILD_BINARY_PATH for the npm package), an x86-64 binary. They are
    # scripts, so the path is not on any ninja command line. Same version
    # (0.25.1), which the npm package's JavaScript checks.
    devtools=${S}/src/third_party/devtools-frontend/src
    ln -sfn ${STAGING_BINDIR_NATIVE}/esbuild $devtools/third_party/esbuild/esbuild

    # rollup: see SRC_URI above. Replace only the native loader, after checking
    # that the rest of the package is the release we fetched.
    wasm=${UNPACKDIR}/rollup-wasm-node/package
    grep -q '"version": "4.60.4"' $devtools/node_modules/rollup/package.json || \
        bbfatal "devtools-frontend's rollup is no longer 4.60.4; update rollup-wasm-node"
    cmp -s $wasm/dist/shared/rollup.js $devtools/node_modules/rollup/dist/shared/rollup.js || \
        bbfatal "rollup-wasm-node's rollup.js differs from devtools-frontend's"
    install -m 0644 $wasm/dist/native.js $devtools/node_modules/rollup/dist/native.js
    install -d $devtools/node_modules/rollup/dist/wasm-node
    install -m 0644 $wasm/dist/wasm-node/* $devtools/node_modules/rollup/dist/wasm-node/

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

# The V8 snapshot tools run on the build host, so build them for its CPU rather
# than always for x86-64 (aarch64 build hosts).
SRC_URI:append = " \
    file://0001-cros-build-the-V8-snapshot-toolchain-for-the-build-host-CPU.patch \
"

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
