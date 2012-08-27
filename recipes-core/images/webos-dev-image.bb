#
# This is the the openwebos-ports development image not suited for release
#

require recipes-core/images/webos-image.bb

IMAGE_FEATURES += "debug-tweaks"

MACHINE_EXTRA_INSTALL = ""
MACHINE_EXTRA_INSTALL_tuna = " \
  libsamsung-ipc \
  libsamsung-ipc-tools \
  samsung-fb-console \
  brcm-patchram-plus"

IMAGE_INSTALL += " \
  opkg \
  mingetty \
  serial-forward \
  \
  ${MACHINE_EXTRA_INSTALL}"
