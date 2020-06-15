SUMMARY = "A terminal emulator QML widget, based on LXQt's QTermWidget"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4641e94ec96f98fabc56ff9cc48be14b"

PV = "0.14.1+git${SRCPV}"
SRCREV = "59f967d5e1f6e9ce8e1632d9405422b071d93d30"

DEPENDS = "qtbase qtdeclarative"
RDEPENDS_${PN} = "ttf-liberation-mono qtxmlpatterns"

SRC_URI = " \
    git://github.com/Swordfish90/qmltermwidget.git;protocol=git;branch=master \
    file://0001-qmltermwidget.pro-don-t-install-asset-directories-tw.patch \
"
S = "${WORKDIR}/git"

inherit qmake5

FILES_${PN} += "${libdir}"
