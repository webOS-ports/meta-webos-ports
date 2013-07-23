FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# We're not yet using xkb for anything so disable it
XKB_DEPENDS = ""

SRC_URI = " \
    git://gitorious.org/qt/qtwayland.git;protocol=git;branch=master \
    file://0001-Import-various-changes-from-Mer-s-variant-of-qtwayla.patch"

SRCREV = "fa9c133cc3799bfcf027e6b3d4983bd4cfd5db6d"
