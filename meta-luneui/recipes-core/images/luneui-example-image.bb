SUMMARY = "LuneUI example image"

IMAGE_FEATURES += "package-management allow-empty-password empty-root-password allow-root-login post-install-logging ssh-server-openssh"

inherit core-image

MESA_PKGS = " \
    libegl-mesa \
    libgles2-mesa \
    libgbm \
    mesa-megadriver \
    libgles1-mesa \
    libglapi \
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

IMAGE_INSTALL:append:qemux86-64 = " \
    anbox \
"
