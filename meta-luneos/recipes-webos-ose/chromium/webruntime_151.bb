# Copyright (c) 2024-2026 LG Electronics, Inc.

WEBRUNTIME_REPO_VERSION = "151"

require webruntime.inc
require webruntime-repo${REPO_VERSION}.inc

PROVIDES = "virtual/webruntime"

PR = "${INC_PR}.0"

# webruntime-common.inc inherits webos_enhanced_submissions, which exists to
# sanity-check git SRC_URIs against the submission tag in WEBOS_VERSION. 151 is
# assembled from the upstream tarball, so there is no git repository for it to
# check and it reports "shouldn't be inherited here (it has nothing to do)".
# luneos.inc puts that check in ERROR_QA distro-wide, which is right for the
# repo-based recipes; demote it for this one rather than weakening it globally
# or restructuring the shared .inc that 108 and 120 also use. The class is
# still wanted: PV is derived through it from WEBOS_VERSION.
ERROR_QA:remove = "webos-enh-sub-warning buildpaths"

# buildpaths: the -dbg package's debug info references TMPDIR, but the paths are
# not ours - they come from compiler-rt, which gets linked in as
# libclang_rt.builtins.a and carries its own build paths:
#   TMPDIR/work-shared/llvm-project-source-22.1.8-r0/.../compiler-rt/lib/builtins/divti3.c
#   TMPDIR/work/x86_64-linux/compiler-rt-native/22.1.8/build
# It only appeared once the world rebuild rebuilt compiler-rt-native. This is
# debug-info metadata that affects build reproducibility, not the shipped
# binaries or anything at runtime, and the fix belongs in compiler-rt's own
# recipe rather than here - so warn instead of failing the build.
WARN_QA:append = " buildpaths"
WARN_QA:append = " webos-enh-sub-warning"

# bitbake picks the highest version available for virtual/webruntime, so
# without this the mere presence of these recipes would take the 120 build
# away. Select 151 deliberately by requiring
# conf/distro/include/luneos-webruntime-151.inc, which sets
#
#   PREFERRED_VERSION_webruntime:qemux86-64       = "151.%"
#   PREFERRED_VERSION_webruntime-clang:qemux86-64 = "151.%"
#
# (PREFERRED_VERSION takes a PN, not a virtual - luneos-preferred-versions.inc
# pins webruntime and webruntime-clang by name, and pinning the virtual has no
# effect.)
# No COMPATIBLE_MACHINE override: webruntime-common.inc already opens every
# arch LuneOS builds, and 151 is now the default everywhere rather than a
# qemux86-64 build test. The x86-64 pin that used to live here is gone.

PACKAGECONFIG[debug] = "symbol_level=2 optimize_for_size=true use_debug_fission=true,symbol_level=0"
PACKAGECONFIG[debug-blink] = "blink_symbol_level=2,blink_symbol_level=1"

PACKAGECONFIG[v8_lite] = "v8_enable_lite_mode=true,v8_enable_lite_mode=false"

GN_ARGS:append = " neva_dcheck_always_on=true"
GN_ARGS:append = " use_x11=false"
PACKAGECONFIG[google_ozone_wayland] = "import(\"//neva/gow.gn\")"
PACKAGECONFIG += "google_ozone_wayland"

# intel_ozone_wayland is deliberately NOT enabled on 151, unlike 108/120.
#
# //neva/iow.gn sets ozone_platform_wayland_external=true, which builds LG's
# legacy ozone-wayland backend (//ozone:ozone_wayland). That backend declares
# 75 messages with IPC_MESSAGE_CONTROLn, and M151 deleted the legacy IPC
# message system outright - not just the macros: IPC::Message itself is now
# "nothing more than a pickle with an attachment set", with its routing/type/
# flags fields reduced to pad_* placeholders that mojo relies on, and
# GpuPlatformSupport's IPC transport is gone from //ui/ozone too. Reviving it
# would mean forking IPC::Message away from the one mojo uses.
#
# Nothing selects it at runtime: WAM launches with --ozone-platform=wayland,
# and "wayland" is index 0 in the generated platform list so it is also the
# default when that flag is absent. Every use of GpuPlatformSupport is already
# guarded by #if defined(OZONE_PLATFORM_WAYLAND_EXTERNAL), so this removes the
# whole legacy surface cleanly.
#
# iow.gn's other setting, use_xkbcommon, is still wanted, so it is set here.
# VERIFY ON DEVICE: if anything on LuneOS does select wayland_external, this is
# where to look first.
PACKAGECONFIG[intel_ozone_wayland] = "import(\"//neva/iow.gn\")"
GN_ARGS:append = " use_xkbcommon=true"

GN_ARGS:append = " \
    libdir=\"${libdir}\"\
    includedir=\"${includedir}\"\
    enable_mojom_closure_compile=false\
    enable_js_type_check=false\
    use_neva_media_player_camera=true\
    system_wayland_scanner_path=\"${STAGING_BINDIR_NATIVE}/wayland-scanner\" \
"

# M151 generates gender-aware string variants and wants a set of .grd inputs
# that our string set does not provide. Off, as in the port tree's args.gn.
GN_ARGS:append = " translate_genders=false"

# VA-API. M151 turns use_vaapi on by default for x64 linux; 120's default was
# off, which is why the 120 recipe never mentions it. The wrynose sysroot ships
# no libva, so leaving it on fails at do_configure in
# //build/config/linux/pkg_config.gni with "Script returned non-zero exit code"
# for pkg_config("libva").
GN_ARGS:append = " use_vaapi=false"

# CDM. neva.gni defaults use_neva_cdm to use_neva_media, which is on here, so it
# has to be turned off explicitly. LuneOS ships no CDM - there is no widevine or
# playready recipe anywhere in meta-luneos and the 120 build produces no CDM
# artifacts. Leaving it on pulls in LG's webOS CDM interface, which collides
# with upstream's and needs LG's TV DRM integration to build.
# neva/app_shell/common/shell_content_client.cc guards its
# cdm::AddContentDecryptionModules() call on USE_NEVA_CDM to match.
GN_ARGS:append = " use_neva_cdm=false"

# V8 thread-local storage model.
# ------------------------------
# M151's V8 declares g_current_isolate_ and LocalHeap::current_local_heap_ as
# thread_locals carrying __attribute__((tls_model(V8_TLS_MODEL))). Off
# component that resolves to "local-exec", which emits R_X86_64_TPOFF32
# relocations, and those cannot appear in a shared object. webOS links all of
# V8 into libcbe.so with is_component_build=false, so without this the link
# fails with "relocation R_X86_64_TPOFF32 against g_current_isolate_ can not be
# used when making a shared object". Selecting the library mode gives
# "local-dynamic" plus out-of-line accessors, exactly what COMPONENT_BUILD
# does. The arg itself is added by the neva v8 patch (0016); upstream only
# reached the same define through v8_monolithic. M120 had no such thread_local.
GN_ARGS:append = " v8_tls_used_in_library=true"

# BackupRefPtr.
# -------------
# M151 turned BRP on by default here (enable_backup_ref_ptr_support_default is
# now use_partition_alloc_as_malloc_default); M120 gated it behind
# _is_brp_supported and it was off. ContentMainParams::argv also became a
# raw_ptr in M151, so app_shell's and browser_shell's main() instantiate
# RawPtrBackupRefImpl and need PartitionAddressSpace::setup_ and
# RawPtrBackupRefImpl::Acquire/ReleaseInternal - which live inside libcbe.so
# with hidden visibility and have no export macro in a non-component build.
#
# TRADE-OFF: this turns off a use-after-free mitigation that a stock M151
# desktop build has. It restores the M120 behaviour rather than regressing
# against what LuneOS shipped, but it is a deliberate choice, not a detail.
# Keeping BRP would mean exporting PartitionAlloc from libcbe.so, which is
# invasive and would need redoing at every uprev.
GN_ARGS:append = " enable_backup_ref_ptr_support=false"

# pkg-config sysroot paths.
# -------------------------
# M151 took https://crrev.com/c/6506002, which made pkg_config.gni pass the
# sysroot to pkg-config.py relative to root_build_dir instead of absolute:
#
#   _pkg_config_requires_abs_path = pkg_config != "" ||
#       (current_toolchain == host_toolchain && host_pkg_config != "")
#   ...
#   _rebased_sysroot = rebase_path(sysroot, root_build_dir)   # the else branch
#
# M120 always used rebase_path(sysroot). We set host_pkg_config but not
# pkg_config, and on the target toolchain current_toolchain != host_toolchain,
# so the flag comes out false and every -L/-isystem pkg-config returns is
# relative. GN then resolves each one against the directory of the BUILD.gn
# that asked, producing paths like src/media/recipe-sysroot/usr/lib that do not
# exist. It is invisible for libraries in a default search path, because
# --sysroot already covers /usr/lib - but libgmp-player-client.so lives in
# ${libdir}/cbe, so it is the one that fails, with:
#
#   mold: fatal: library not found: gmp-player-client
#
# Naming the tool flips the flag back and restores absolute paths. It does not
# bypass pkg-config.py: pkg_config only appends "-p pkg-config" to the wrapper's
# argument list, so the sysroot handling in that script still runs.
GN_ARGS:append = " pkg_config=\"pkg-config\""

# Note on the cros_* block in webruntime-common.inc
# -------------------------------------------------
# cros_host_ar/cc/cxx/ld/nm, cros_host_is_clang and cros_target_ar/cc/cxx are
# all inert under 151: nothing in the tree reads them any more (gn warns about
# cros_host_ar and silently ignores the rest). LG's
# //build/toolchain/cros/BUILD.gn now uses clang_toolchain(), which takes its
# compiler from clang_base_path instead. They are left in place because
# webruntime-common.inc is shared with the 108 and 120 recipes, where they
# still matter - they simply have no effect here.
#
# v8_snapshot_toolchain is still needed: the label resolves through
# clang_toolchain("v8_snapshot_clang_${target_cpu}") in that same file.

# Disable Advanced Encryption Standard features set for ARM in zlib component
GN_ARGS_AES = "disable_zlib_arm_aes_cflag=true"
GN_ARGS += "${GN_ARGS_AES}"

# Raspberry Pi does not support PMULL.
GN_ARGS_AES:raspberrypi4-64 = ""
GN_ARGS_NEON:raspberrypi4-64 = "arm_use_neon=false"
GN_ARGS_AES:raspberrypi3-64 = ""
GN_ARGS_NEON:raspberrypi3-64 = "arm_use_neon=false"
