SUMMARY = "A terminal emulator QML widget, based on LXQt's QTermWidget"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0.14.1+git${SRCPV}"
SRCREV = "8dd4af67dcb0dd849d11decb283633f20249a9df"

DEPENDS = "qtbase qtdeclarative"
RDEPENDS:${PN} = "ttf-liberation-mono"

SRC_URI = " \
    git://github.com/herrie82/qmltermwidget.git;protocol=https;branch=herrie/qt6 \
    file://0001-qmltermwidget.pro-don-t-install-asset-directories-tw.patch \
"
S = "${WORKDIR}/git"

inherit qt6-qmake

FILES:${PN} += "${libdir}"
