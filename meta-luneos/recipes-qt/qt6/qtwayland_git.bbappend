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

SRC_URI:append = " \
    file://0004-QWaylandDisplay-don-t-ignore-wayland-QT_IM_MODULE.patch;maxver=6.7.* \
    file://0004-QWaylandDisplay-don-t-ignore-wayland-QT_IM_MODULE-6.8.x.patch;minver=6.8.0 \
"

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
PACKAGECONFIG = "wayland-server wayland-client client-wl-shell ${PACKAGECONFIG_DMABUF}"

PACKAGECONFIG_DMABUF = "drm-egl-server-buffer"

# libhybris's EGL/eglext.h doesn't define EGL_LINUX_DMA_BUF_EXT/EGL_EXT_image_dma_buf_import_modifiers tested by qtwayland
# tests HAVE_dmabuf_server_buffer/HAVE_dmabuf_client_buffer
# we can try to enable this again after upgrading libhybris to have:
# https://github.com/libhybris/libhybris/commit/95b1472814c6cec192eef2da3361c81fa3d91860
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
