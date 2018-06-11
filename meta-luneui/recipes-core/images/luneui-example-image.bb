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
    qt5-plugin-generic-vboxtouch \
    anbox \
    anbox-data \
"

IMAGE_INSTALL_append_qemux86-64 = " \
    kernel-module-ashmem-linux \
    kernel-module-binder-linux \
    kernel-module-squashfs \
"
