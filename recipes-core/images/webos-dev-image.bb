#
# This is the the openwebos-ports development image not suited for release
#

require recipes-core/images/webos-image.bb

IMAGE_FEATURES += "debug-tweaks"

IMAGE_INSTALL += " \
  opkg \
  samsung-fb-console \
  mingetty \
  serial-forward \
"
