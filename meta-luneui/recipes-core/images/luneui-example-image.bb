SUMMARY = "LuneUI example image"

IMAGE_FEATURES += "package-management debug-tweaks ssh-server-openssh"

inherit core-image

IMAGE_INSTALL += " \
    qtbase \
    qtbase-examples \
    qtbase-plugins \
"
