SUMMARY = "LuneUI example image"

# No passwordless root by default, as in luneos_image.bbclass: without
# allow-empty-password, empty-root-password and allow-root-login oe-core locks
# root and sshd keeps PermitRootLogin prohibit-password. LUNEOS_DEBUG_ACCESS =
# "1" in local.conf (luneos-features.inc) brings them back for a local
# developer build.
IMAGE_FEATURES += "package-management post-install-logging ssh-server-openssh \
    ${@oe.utils.conditional('LUNEOS_DEBUG_ACCESS', '1', 'allow-empty-password empty-root-password allow-root-login', '', d)} \
"

inherit core-image

MESA_PKGS = " \
    libegl-mesa \
    libgles2-mesa \
    libgbm \
    mesa-megadriver \
    libgles1-mesa \
"

IMAGE_INSTALL += " \
    qtbase \
    qtbase-examples \
    qtbase-plugins \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'weston weston-init weston-examples', '', d)} \
    gtk+3-demo \
    glmark2 \
    kernel-modules \
    luna-next-cardshell \
    luneos-components \
"

IMAGE_INSTALL:append:qemuall = " \
    ${MESA_PKGS} \
    v86d \
    qt-plugin-generic-vboxtouch \
"

