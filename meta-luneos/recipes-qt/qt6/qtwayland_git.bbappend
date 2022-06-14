# Copyright (c) 2013-2022 LG Electronics, Inc.

inherit webos_qt_global

EXTENDPRAUTO:append = "webos30"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PATCHTOOL = "git"

# Upstream-Status: Backport
SRC_URI:append = " \
    file://0001-Support-presentation-time-protocol.patch;maxver=6.2.* \
    file://0002-Use-scope-resolution-operator-for-request.patch;maxver=6.2.* \
    file://0003-Fix-to-have-presentation-feedback-sequence-timely.patch;maxver=6.3.1 \
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

PACKAGECONFIG_DMABUF = "dmabuf-client-buffer dmabuf-server-buffer drm-egl-server-buffer"
# libhybris's EGL/eglext.h doesn't define EGL_LINUX_DMA_BUF_EXT/EGL_EXT_image_dma_buf_import_modifiers tested by qtwayland
# tests HAVE_dmabuf_server_buffer/HAVE_dmabuf_client_buffer
# we can try to enable this again after upgrading libhybris to have:
# https://github.com/libhybris/libhybris/commit/95b1472814c6cec192eef2da3361c81fa3d91860
PACKAGECONFIG_DMABUF:halium = ""

# qtwayland-qmlplugins is not used in webos
RRECOMMENDS:${PN}:remove = "${PN}-qmlplugins"

do_install:append() {
    # Remove files unnecessary or conflict with qtwayland-webos
    rm -rf ${D}${QT6_INSTALL_PLUGINSDIR}/platforms \
        ${D}${QT6_INSTALL_PLUGINSDIR}/{wayland-decoration-client,wayland-graphics-integration-client} \
        ${D}${QT6_INSTALL_PLUGINSDIR}/wayland-graphics-integration-server/libqt-wayland-compositor-wayland-eglstream-controller.so
}
