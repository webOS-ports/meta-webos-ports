SUMMARY = "LuneUI example image"

IMAGE_FEATURES += "package-management debug-tweaks ssh-server-openssh"

inherit core-image

MESA_PKGS = " \
    libegl-mesa \
    libgles2-mesa \
    libgbm \
    mesa-megadriver \
    mesa \
    libgles1-mesa \
    libglapi \
    libwayland-egl \
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

IMAGE_INSTALL_append_qemuall = " \
    ${MESA_PKGS} \
    vboxguestdrivers \
    v86d \
    qt5-plugin-generic-vboxtouch \
"
