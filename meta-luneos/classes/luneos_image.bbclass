FEATURE_PACKAGES_luneos-extended = "packagegroup-luneos-extended"
FEATURE_PACKAGES_luneos-development = "packagegroup-luneos-development"

LUNEOS_IMAGE_DEFAULT_FEATURES = "debug-tweaks ssh-server-openssh package-management luneos-extended"

# We're using devtmpfs so are not required to have a minimal populated /dev in our images
USE_DEVFS = "1"

inherit webos_image

IMAGE_INSTALL_append = " \
  ${MACHINE_EXTRA_RDEPENDS} \
"

WEBOS_MACHINE_NO_SWAP_PARTITION ??= "1"

# On the touchpad we have our a LVM partition used as SWAP space
WEBOS_MACHINE_NO_SWAP_PARTITION_tenderloin = "0"
# For our emulator we don't need SWAP at all
WEBOS_MACHINE_NO_SWAP_PARTITION_qemux86 = "0"
WEBOS_MACHINE_NO_SWAP_PARTITION_qemux86-64 = "0"

# Enable swap image to extend available memory
webos_swap_hook() {
    if [ "${WEBOS_MACHINE_NO_SWAP_PARTITION}" == "1" ] ; then
        echo "/SWAP.img none swap sw 0 0" >> ${IMAGE_ROOTFS}/etc/fstab
    fi
}

ROOTFS_POSTPROCESS_COMMAND += "webos_swap_hook;"
