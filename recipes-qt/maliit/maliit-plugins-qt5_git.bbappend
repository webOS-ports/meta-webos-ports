# We only want the maliit keyboard so disable the nemo one
EXTRA_QMAKEVARS_PRE += "CONFIG+=disable-nemo-keyboard"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://0001-maliit-keyboard-disable-setting-a-different-surface-.patch"
