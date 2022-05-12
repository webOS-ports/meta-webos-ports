SUMMARY = "QCamera plugin for the qtubuntu camera backend"
HOMEPAGE = "http://bazaar.launchpad.net/~phablet-team/qtubuntu-camera"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

inherit qt6-qmake pkgconfig

DEPENDS = "libhybris qtbase libqtubuntu-media-signals exiv2 qtmultimedia pulseaudio"

RDEPENDS:${PN} += "qtmultimedia-plugins libpulse-simple0"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

SRCREV = "168"
SRC_URI = "bzr://bazaar.launchpad.net/~phablet-team/qtubuntu-camera/trunk"
PV = "0.3.3+bzr${SRCREV}"

S = "${WORKDIR}/trunk"

EXTRA_OEMAKE += "INSTALL_ROOT=${D}"

EXTRA_QMAKEVARS_PRE = "\
    PREFIX=${prefix} \
"

FILES:${PN} += "\
    ${OE_QMAKE_PATH_PLUGINS} \
"

do_configure:prepend() {
    # Empty .qmake.conf, to avoid conflicts with pkgconfig from Yocto
    echo "" > ${S}/.qmake.conf
}
