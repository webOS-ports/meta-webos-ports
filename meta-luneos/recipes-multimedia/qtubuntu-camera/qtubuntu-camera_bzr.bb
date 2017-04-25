SUMMARY = "QCamera plugin for the qtubuntu camera backend"
HOMEPAGE = "http://bazaar.launchpad.net/~phablet-team/qtubuntu-camera"
LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

inherit qmake5

DEPENDS = "libhybris qtbase libqtubuntu-media-signals exiv2 qtmultimedia"

RDEPENDS_${PN} += "qtmultimedia-plugins libpulse-simple0"

SRCREV = "168"
SRC_URI = "bzr://bazaar.launchpad.net/~phablet-team/qtubuntu-camera/trunk"
PV = "0.3.3+bzr${SRCREV}"

S = "${WORKDIR}/trunk"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"

EXTRA_QMAKEVARS_PRE = "\
    PREFIX=${prefix} \
"

FILES_${PN} += "\
            ${libdir}/qt5/plugins \
"

