# in meta-oe recipe this is controlled by 'qt' DISTRO_FEATURE which we don't use
DEPENDS += "qtbase qtbase-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI:append:pinephone = " file://0001-feature-pinephone-branch.patch"
