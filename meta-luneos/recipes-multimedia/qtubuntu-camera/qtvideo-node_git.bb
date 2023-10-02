SUMMARY = "VideoOutput plugin for the camera backend"
HOMEPAGE = "http://bazaar.launchpad.net/~phablet-team/qtvideo-node"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

inherit qt6-qmake

DEPENDS = "libhybris qtbase libqtubuntu-media-signals exiv2 qtmultimedia qtdeclarative"

RDEPENDS:${PN} += "qtmultimedia-plugins"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

SRCREV = "d47a9b1eba42c0e5ba6f2e236f28bb779c9ba1cf"
SRC_URI = "git://github.com/webOS-ports/luneos-qtvideo-node.git;branch=master;protocol=https"
PV = "0.2.1+git"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"

EXTRA_QMAKEVARS_PRE = "\
    PREFIX=${prefix} \
"

FILES:${PN} += "\
    ${OE_QMAKE_PATH_PLUGINS} \
"
