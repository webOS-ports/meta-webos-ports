DESCRIPTION = "This QPA plugin allows rendering on top of libhybris-based hwcomposer EGL \
platforms. The hwcomposer API is specific to a given Droid release, and \
sometimes also SoC type (generic, qcom, exynos4, ...)."
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://hwcomposer_backend.cpp;beginline=1;endline=40;md5=09c08382077db2dbc01b1b5536ec6665"

PV = "6.3.0+git"
# 2026-09-05: bumped from 998956a to current upstream tip (15 commits) as
# part of the bluejay (Pixel 6a, Mali-G78/Valhall, Android 16, pure-HWC2)
# display investigation. Two are directly relevant to "compositor reports
# ready, never crashes, never paints a second frame":
#   f45067b - stop unconditionally loading a fake libminisf SurfaceFlinger
#     shim before creating the HWC2 backend. Upstream's own message: "In
#     hwc2 all this is not needed anymore as the hwc2 module is part of a
#     separate composer service. This loading libminisf also causes issues
#     with latest Android 14 base." Bluejay is a pure-HWC2 device
#     (composer is its own real service) on an even newer Android base.
#   a1654af - fix HWC2 present-fence handling in HwComposerBackend_v20::
#     present(). The old code does sync_wait(fence, -1) - an INFINITE
#     blocking wait - on a fence tracked against the wrong buffer
#     generation (HWC2 present fences signal frame N shown/frame N-1
#     buffer free, not HWC1 release-fence semantics). A live-locked
#     present() on frame 2 would look exactly like "first frame (the
#     existing boot logo) stays on screen forever" from the outside - the
#     process never crashes (matches cycle 51's captured evidence), it
#     just never completes another frame.
# Also picks up e73333d (slot-based HWC2 client-target buffer cache,
# matching real Android's caching behavior - verified its prerequisite,
# the `slot` param on hwc2_compat_display_set_client_target, is already
# present in this project's current libhybris SRCREV) and two small/
# harmless ones (v10 refresh-rate-unit fix, QPlatformScreen::setPowerState).
SRCREV = "31f4f6506702bd872ac47305fc8f8d75f2bb047b"

DEPENDS = "qtbase libhybris qtwayland virtual/android-headers"

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

SRC_URI = " \
    git://github.com/mer-hybris/qt5-qpa-hwcomposer-plugin.git;branch=master;protocol=https \
    file://0001-hwcomposer_backend_v20-fix-present-validate-and-scre.patch;patchdir=.. \
"
S = "${WORKDIR}/git/hwcomposer"

inherit webos_ports_fork_repo
inherit qt6-qmake pkgconfig

# WARNING: The recipe qt5-qpa-hwcomposer-plugin is trying to install files into a shared area when those files already exist. Those files and their manifest location are:
#   /OE/build/owpb/webos-ports/tmp-eglibc/sysroots/tenderloin/usr/lib/cmake/Qt5Gui/Qt5Gui_QEglFSIntegrationPlugin.cmake
#   Matched in manifest-tenderloin-qtbase.populate_sysroot
#   Please verify which package should provide the above files.
do_install:append() {
    rm -vf ${D}${libdir}/cmake/Qt5Gui/Qt5Gui_QEglFSIntegrationPlugin.cmake
}

FILES:${PN} += "${OE_QMAKE_PATH_PLUGINS}/platforms/libhwcomposer.so"
FILES:${PN}-dev += "${libdir}/cmake"
