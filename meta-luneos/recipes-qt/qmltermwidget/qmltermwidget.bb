SUMMARY = "A terminal emulator QML widget, based on LXQt's QTermWidget"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0.14.1+git${SRCPV}"
SRCREV = "1e7ab36e9894f9d7c5b7d82267faba1e60efd635"

DEPENDS = "qtbase qtdeclarative qt5compat"
RDEPENDS:${PN} = "ttf-liberation-mono"

SRC_URI = " \
    git://github.com/Tofee/qmltermwidget.git;protocol=https;branch=tofe/qt6 \
    file://0001-qmltermwidget.pro-don-t-install-asset-directories-tw.patch \
"
S = "${WORKDIR}/git"

inherit qt6-qmake

FILES:${PN} += "${libdir}"
