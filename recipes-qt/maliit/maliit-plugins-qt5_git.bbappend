# Don't build extra stuff that we won't use
EXTRA_QMAKEVARS_PRE += "CONFIG+=notests"

# Point to our ubuntu-keyboard fork
SRC_URI = "git://github.com/webOS-ports/webos-keyboard;branch=master"
SRCREV = "f56014a3434923e635190c3055bdbdfcdf012e39"

# Put in the correct license info
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=6a6a8e020838b23406c81b19c1d46df6"

# Enable support for predictive text and word correction
EXTRA_QMAKEVARS_PRE += "CONFIG+=enable-presage CONFIG+=enable-hunspell"
DEPENDS += "hunspell presage"


