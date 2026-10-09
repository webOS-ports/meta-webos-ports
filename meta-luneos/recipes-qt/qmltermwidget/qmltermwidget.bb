SUMMARY = "A terminal emulator QML widget, based on LXQt's QTermWidget"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4641e94ec96f98fabc56ff9cc48be14b"

# QMLTermWidget 2.0: Qt 6 without Qt5Compat
PV = "2.0+git"
SRCREV = "8913504fa2ebd220ebe7c680c32954e1b3c035c5"

DEPENDS = "qtbase qtdeclarative"
RDEPENDS:${PN} = "ttf-liberation-mono"

SRC_URI = " \
    git://github.com/Swordfish90/qmltermwidget.git;protocol=https;branch=master \
    file://0001-qmltermwidget.pro-don-t-install-asset-directories-tw.patch \
"

inherit qt6-qmake

FILES:${PN} += "${libdir}"
