SUMMARY = "A terminal emulator QML widget, based on LXQt's QTermWidget"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4641e94ec96f98fabc56ff9cc48be14b"

# QMLTermWidget 2.0 (Qt 6 without Qt5Compat), with the webOS-ports audit
# fixes and the install fix carried as commits
PV = "2.0+git"
SRCREV = "f9845c4b0cda0cd96498fb2bee45ed7b6fdcce25"

DEPENDS = "qtbase qtdeclarative"
RDEPENDS:${PN} = "ttf-liberation-mono"

SRC_URI = "git://github.com/webOS-ports/qmltermwidget.git;protocol=https;branch=herrie/audit"

inherit qt6-qmake

FILES:${PN} += "${libdir}"
