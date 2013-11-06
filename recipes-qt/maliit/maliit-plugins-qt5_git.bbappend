# We only want the maliit keyboard so disable the nemo one
EXTRA_QMAKEVARS_PRE += "CONFIG+=disable-nemo-keyboard"

# Enable support for predictive text and word correction
EXTRA_QMAKEVARS_PRE += "CONFIG+=enable-pressage CONFIG+=enable-hunspell"
DEPENDS += "hunspell presage"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://0001-maliit-keyboard-disable-setting-a-different-surface-.patch \
            file://0002-maliit-disable-secondary-overlays.patch \
            file://0003-maliit-fix-orientation.patch"
