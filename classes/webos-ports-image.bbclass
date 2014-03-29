FEATURE_PACKAGES_webos-ports-extended = "packagegroup-webos-ports-extended"
FEATURE_PACKAGES_webos-ports-development = "packagegroup-webos-ports-development"

WEBOS_PORTS_IMAGE_DEFAULT_FEATURES = "debug-tweaks ssh-server-openssh package-management webos-ports-extended"

# We're using devtmpfs so are not required to have a minimal populated /dev in our images
USE_DEVFS = "1"

inherit webos-image

IMAGE_INSTALL_append = " \
  ${MACHINE_EXTRA_RDEPENDS} \
"

# Enable swap image to extend available memory
webos_swap_hook() {
    echo "/SWAP.img none swap sw 0 0" >> ${IMAGE_ROOTFS}/etc/fstab
}

ROOTFS_POSTPROCESS_COMMAND += "webos_swap_hook;"
