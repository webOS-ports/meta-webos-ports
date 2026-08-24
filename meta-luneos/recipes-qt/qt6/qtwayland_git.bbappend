# Copyright (c) 2013-2023 LG Electronics, Inc.

inherit webos_qt_global

EXTENDPRAUTO:append = "webos41"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PATCHTOOL = "git"

# Upstream-Status: Inappropriate
# NOTE: Increase maxver when upgrading Qt version
# Resolved differently for LuneOS with one-line change in
# 0004-QWaylandDisplay-don-t-ignore-wayland-QT_IM_MODULE.patch
# SRC_URI:append = " \
#     file://0005-Revert-Also-use-text-input-if-QT_IM_MODULE-is-empty-.patch;maxver=6.5.3 \
# "

# From 6.10 the QtWayland client lives in qtbase, so this change moved to
# meta-luneos/recipes-qt/qt6/qtbase/9906-QWaylandDisplay-*.patch and the
# patches below are capped at 6.9.
SRC_URI:append = " \
    file://0004-QWaylandDisplay-don-t-ignore-wayland-QT_IM_MODULE.patch;maxver=6.7.* \
    file://0004-QWaylandDisplay-don-t-ignore-wayland-QT_IM_MODULE-6.8.x.patch;minver=6.8.0;maxver=6.9.* \
"

# The compositor half of the libhybris server-buffer integration still expects
# QOpenGLTexture in QtGui, where it has not lived since Qt 6.0. Only halium
# machines build it, so upstream never sees it break. The client half has the
# same problem plus a protocol one, patched in qtbase as 9907.
SRC_URI:append = " file://0006-libhybris-egl-server-take-QOpenGLTexture-from-QtOpenG.patch"

# More options for fine-tuned configuration
PACKAGECONFIG[brcm] = "-DFEATURE_wayland_brcm=ON,-DFEATURE_wayland_brcm=OFF,"
PACKAGECONFIG[drm-egl-server-buffer] = "-DFEATURE_wayland_drm_egl_server_buffer=ON,-DFEATURE_wayland_drm_egl_server_buffer=OFF,"
PACKAGECONFIG[libhybris-egl-server-buffer] = "-DFEATURE_wayland_libhybris_egl_server_buffer=ON,-DFEATURE_wayland_libhybris_egl_server_buffer=OFF,"
PACKAGECONFIG[shm-emulation-server-buffer] = "-DFEATURE_wayland_shm_emulation_server_buffer=ON,-DFEATURE_wayland_shm_emulation_server_buffer=OFF,"
PACKAGECONFIG[vulkan-server-buffer] = "-DFEATURE_wayland_vulkan_server_buffer=ON,-DFEATURE_wayland_vulkan_server_buffer=OFF,"
PACKAGECONFIG[client-fullscreen-shell-v1] = "-DFEATURE_wayland_client_fullscreen_shell_v1=ON,-DFEATURE_wayland_client_fullscreen_shell_v1=OFF,"
PACKAGECONFIG[client-ivi-shell] = "-DFEATURE_wayland_client_ivi_shell=ON,-DFEATURE_wayland_client_ivi_shell=OFF,"
PACKAGECONFIG[client-wl-shell] = "-DFEATURE_wayland_client_wl_shell=ON,-DFEATURE_wayland_client_wl_shell=OFF,"
PACKAGECONFIG[client-xdg-shell] = "-DFEATURE_wayland_client_xdg_shell=ON,-DFEATURE_wayland_client_xdg_shell=OFF,"

# PACKAGECONFIG for webos
# wayland-server/wayland-client dropped: meta-qt6 6.12's qtwayland defines no
# PACKAGECONFIG[] at all, and an entry with no definition is a fatal QA error
# (the same one that stopped qtbase over "examples"). Qt 6.10 moved the Wayland
# client into qtbase and builds the compositor unconditionally, so there is
# nothing left for these two to switch. client-wl-shell and the DMABUF entry
# stay - this bbappend defines those itself, just above.
PACKAGECONFIG = "client-wl-shell ${PACKAGECONFIG_DMABUF}"

# The zwp_linux_dmabuf_v1 compositor integration. Both plugins built from it -
# linux-dmabuf-unstable-v1 and linux-dmabuf-v1 - link Libdrm::Libdrm, and the
# configure test for the feature does too, so without libdrm in DEPENDS Qt just
# reports:
#   -- Could NOT find Libdrm (missing: Libdrm_LIBRARY Libdrm_INCLUDE_DIR)
#   -- Performing Test Linux Client dma-buf Buffer Sharing - Failed because
#      Libdrm::Libdrm not found
#          Linux dma-buf client buffer .......... no
# On Mesa machines it was never noticed because virtual/egl is mesa, which pulls
# libdrm into the sysroot transitively - so the feature auto-detected ON there.
# On halium virtual/egl is libhybris, which does not, and the feature silently
# went off. Depend on it explicitly so the outcome is the PACKAGECONFIG's
# decision rather than a side effect of who else happens to need libdrm.
PACKAGECONFIG[dmabuf-client-buffer] = "-DFEATURE_wayland_dmabuf_client_buffer=ON,-DFEATURE_wayland_dmabuf_client_buffer=OFF,libdrm"

PACKAGECONFIG_DMABUF = "drm-egl-server-buffer dmabuf-client-buffer"

# Deliberately off on halium, and not for the reason this comment used to give.
# The libhybris EGL headers have defined EGL_LINUX_DMA_BUF_EXT and
# EGL_EXT_image_dma_buf_import_modifiers since 2020 (commit 95b14728), so the
# headers are no longer the obstacle - the driver underneath them is.
#
# On halium virtual/egl is libhybris, whose eglCreateImageKHR is a passthrough to
# the vendor blob (hybris/egl/egl.c), and the Android GPU drivers implement
# EGL_ANDROID_image_native_buffer (gralloc) rather than EGL_EXT_image_dma_buf_import.
# On sargo, for instance, no library in /android/vendor/lib64/egl mentions
# dma_buf_import at all.
#
# That would be merely useless if the plugin declined to load, but it is worse
# than that: LinuxDmabufClientBufferIntegration::initializeHardware() constructs
# the LinuxDmabuf object - creating and advertising the zwp_linux_dmabuf_v1
# global - before it probes for eglQueryDmaBufFormatsEXT/eglQueryDmaBufModifiersEXT,
# and returns early when they are missing without tearing the global back down.
# Clients would bind a protocol the compositor cannot honour and fail every
# buffer import, instead of falling back to wl_shm as they do today.
PACKAGECONFIG_DMABUF:halium = ""

# qtwayland-qmlplugins is not used in webos
RRECOMMENDS:${PN}:remove = "${PN}-qmlplugins"

# Set QT_SKIP_AUTO_PLUGIN_INCLUSION as otherwise
# QtModulePluginTargets.cmake would complain during
# do_install_ptest_base about missing files that are deleted
# deliberately in do_install:append below.
# See https://codereview.qt-project.org/c/qt/qtbase/+/420212.
EXTRA_OECMAKE:append = " -DQT_SKIP_AUTO_PLUGIN_INCLUSION=ON"

do_install:append() {
    # Remove files unnecessary or conflict with qtwayland-webos
    rm -rf ${D}${QT6_INSTALL_PLUGINSDIR}/platforms \
        ${D}${QT6_INSTALL_PLUGINSDIR}/{wayland-decoration-client,wayland-graphics-integration-client} \
        ${D}${QT6_INSTALL_PLUGINSDIR}/wayland-graphics-integration-server/libqt-wayland-compositor-wayland-eglstream-controller.so
}
