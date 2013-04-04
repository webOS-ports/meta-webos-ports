PACKAGE_GROUP_webos-ports-extended = "packagegroup-webos-ports-extended"
PACKAGE_GROUP_webos-ports-development = "packagegroup-webos-ports-development"

WEBOS_PORTS_IMAGE_DEFAULT_FEATURES = "debug-tweaks ssh-server-openssh package-management webos-ports-extended"

# We're using devtmpfs so are not required to have a minimal populated /dev in our images
USE_DEVFS = "1"

inherit webos-image

IMAGE_INSTALL_append = " \
  ${MACHINE_EXTRA_RDEPENDS} \
"
