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
    luna-next \
    luna-next-cardshell \
    luneos-components \
"

# luna-next-cardshell is needed by default LUNA_NEXT_SHELL "card"
# luneos-component provides LuneOS.Service which is needed by luna-next-cardshell card/qml/LunaSysAPI/Preferences.qml:20
# but luneos-component depends on qtwebengine

IMAGE_INSTALL_append_qemuall = " \
    kernel-modules \
    vboxguestdrivers \
    v86d \
    qt5-plugin-generic-vboxtouch \
"
