FEATURE_PACKAGES_luneos-extended = "packagegroup-luneos-extended"
FEATURE_PACKAGES_luneos-development = "packagegroup-luneos-development"

LUNEOS_IMAGE_DEFAULT_FEATURES = "debug-tweaks ssh-server-openssh package-management luneos-extended"

# We're using devtmpfs so are not required to have a minimal populated /dev in our images
USE_DEVFS = "1"

inherit webos_image

IMAGE_INSTALL_append = " \
  ${MACHINE_EXTRA_RDEPENDS} \
"

WEBOS_MACHINE_READ_ONLY_ENABLED ??= "1"
WEBOS_MACHINE_READ_ONLY_ENABLED_qemuall = "0"

IMAGE_FEATURES += "${@ 'read-only-rootfs' if '${WEBOS_MACHINE_READ_ONLY_ENABLED}' == '1' else ''}"
