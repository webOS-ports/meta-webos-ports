PACKAGE_GROUP_webos-ports-extended = "packagegroup-webos-ports-extended"
PACKAGE_GROUP_webos-ports-development = "packagegroup-webos-ports-development"

WEBOS_PORTS_IMAGE_DEFAULT_FEATURES = "debug-tweaks ssh-server-openssh package-management webos-ports-extended"

inherit webos-image

MACHINE_EXTRA_INSTALL = ""
MACHINE_EXTRA_INSTALL_tuna = " \
  samsung-fb-console \
  brcm-patchram-plus \
  pulseaudio-machine-conf \
  samsung-tuna-firmware-nonfree \
"

IMAGE_INSTALL_append = " \
  packagegroup-webos-upstart \
  ${MACHINE_EXTRA_RDEPENDS} \
  ${MACHINE_EXTRA_INSTALL} \
"
