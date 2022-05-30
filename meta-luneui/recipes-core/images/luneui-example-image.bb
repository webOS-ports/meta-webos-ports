SUMMARY = "LuneUI example image"

IMAGE_FEATURES += "package-management debug-tweaks ssh-server-openssh"

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
    weston \
    weston-init \
    weston-examples \
    gtk+3-demo \
    glmark2 \
    kernel-modules \
    luna-next \
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
