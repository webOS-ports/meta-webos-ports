DESCRIPTION = "Developer webOS Ports image."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

IMAGE_FEATURES += "debug-tweaks ssh-server-openssh package-management"

inherit webos-ports-image

MACHINE_EXTRA_INSTALL = ""
MACHINE_EXTRA_INSTALL_tuna = " \
  samsung-fb-console \
  brcm-patchram-plus \
  pulseaudio-machine-conf \
  samsung-tuna-firmware-nonfree \
"

IMAGE_INSTALL_append = " \
  packagegroup-webos-upstart \
  packagegroup-webos-ports-extended \
  packagegroup-webos-ports-development \
  ${MACHINE_EXTRA_RDEPENDS} \
  ${MACHINE_EXTRA_INSTALL} \
"
