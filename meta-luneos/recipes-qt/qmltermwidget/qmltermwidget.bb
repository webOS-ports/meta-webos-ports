SUMMARY = "A terminal emulator QML widget, based on LXQt's QTermWidget"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4641e94ec96f98fabc56ff9cc48be14b"

# QMLTermWidget 2.0 (Qt 6 without Qt5Compat), with the webOS-ports audit
# fixes and the install fix carried as commits
PV = "2.0+git"
SRCREV = "999f4363d15952d7c9538683bcd86894851148ef"

DEPENDS = "qtbase qtdeclarative"
RDEPENDS:${PN} = "ttf-liberation-mono"

SRC_URI = "git://github.com/webOS-ports/qmltermwidget.git;protocol=https;branch=webOS-ports/master"

inherit qt6-qmake

FILES:${PN} += "${libdir}"
