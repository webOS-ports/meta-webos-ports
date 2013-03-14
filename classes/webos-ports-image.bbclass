PACKAGE_GROUP_webos-ports-extended = "packagegroup-webos-ports-extended"
PACKAGE_GROUP_webos-ports-development = "packagegroup-webos-ports-development"

WEBOS_PORTS_IMAGE_DEFAULT_FEATURES = "debug-tweaks ssh-server-openssh package-management webos-ports-extended"

inherit webos-image

IMAGE_INSTALL_append = " \
  packagegroup-webos-upstart \
  ${MACHINE_EXTRA_RDEPENDS} \
"
