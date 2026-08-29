require luneos-image.inc

DESCRIPTION = "Developers LuneOS image"

IMAGE_FEATURES += "luneos-development"

webos_enable_devmode() {
    install -d ${IMAGE_ROOTFS}/var/luna
    install -d ${IMAGE_ROOTFS}/var/luna/preferences
    touch ${IMAGE_ROOTFS}/var/luna/dev-mode-enabled
    touch ${IMAGE_ROOTFS}/var/luna/preferences/devmode_enabled
    touch ${IMAGE_ROOTFS}/var/luna/preferences/debug_system_apps
    touch ${IMAGE_ROOTFS}/etc/usb-debugging-enabled
    touch ${IMAGE_ROOTFS}/.writable_image
}

ROOTFS_POSTPROCESS_COMMAND += "webos_enable_devmode;"

MESA_PKGS = " \
    libegl-mesa \
    libgles2-mesa \
    libgbm \
    mesa-megadriver \
    libgles1-mesa \
"

IMAGE_INSTALL:append = " \
    libdrm-tests \
    qtbase-examples \
    qtbase-plugins \
    glmark2 \
    kernel-modules \
"

# Legacy PDK/SDL game support: the soft-float ARM sysroot, the Palm-API shims
# and the launcher. Nothing referenced this packagegroup before, so none of it
# reached a device however carefully it was built.
#
# This needs the pdk-armel multiconfig enabled in the build - pdk-sysroot takes
# an mcdepends on it - which means BBMULTICONFIG must list pdk-armel or the
# image will not resolve:
#
#     BBMULTICONFIG = "pdk-armel"
#
# It also means an image build pulls in a second, complete build of a soft-float
# ARM userland, which is not cheap. The alternative is to point
# PDK_SYSROOT_TARBALL at a sysroot built outside BitBake, which skips the
# multiconfig entirely - see the pdk-sysroot recipe.
IMAGE_INSTALL:append = " packagegroup-luneos-pdk"

IMAGE_INSTALL:append:tenderloin = " \
    ${MESA_PKGS} \
    luneos-mainline-debug \
"

IMAGE_INSTALL:append:qemuall = " \
    ${MESA_PKGS} \
"
