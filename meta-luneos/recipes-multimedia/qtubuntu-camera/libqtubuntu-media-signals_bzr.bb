SUMMARY = "Library that coordinates media libraries across thread contexts"
HOMEPAGE = "http://bazaar.launchpad.net/~phablet-team/qtubuntu-media-signals"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

inherit webos_cmake_qt6

DEPENDS = "qtbase"

RDEPENDS:${PN} += "qtmultimedia-plugins"

RPROVIDES:${PN} += "libqtubuntu-media-signals2"

SRCREV = "21"
SRC_URI = "bzr://bazaar.launchpad.net/~phablet-team/qtubuntu-media-signals/trunk"
PV = "0.3+bzr${SRCREV}"

S = "${WORKDIR}/trunk"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"

EXTRA_QMAKEVARS_PRE = "\
    PREFIX=${prefix} \
"

