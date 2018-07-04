SUMMARY = "LuneUI example image"

IMAGE_FEATURES += "package-management debug-tweaks ssh-server-openssh"

inherit core-image

IMAGE_INSTALL += " \
    qtbase \
    qtbase-examples \
    qtbase-plugins \
    weston \
    weston-init \
    weston-examples \
    gtk+3-demo \
    clutter-1.0-examples \
    glmark2 \
    kernel-modules \
    libegl-mesa \
    libgles2-mesa \
    libgbm \
    mesa-megadriver \
    libwayland-egl \
    mesa \
    libgles1-mesa \
    libglapi \
"

IMAGE_INSTALL_append_qemuall = " \
    vboxguestdrivers \
    v86d \
"
