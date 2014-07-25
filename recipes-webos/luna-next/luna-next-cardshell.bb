SUMMARY = "Card shell implementation for the next generation webOS UI"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=bc807597ba062cd149d362d22d3061e7"

RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtgraphicaleffects-qmlplugins \
    qtquickcontrols-qmlplugins \
    libconnman-qt \
    libqofono \
    luna-next \
"

WEBOS_VERSION = "0.1.0-25_5c71a9f2bdda0970ebb242002387fa62e40ebdc1"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_tweaks

SRC_URI = "git://github.com/webOS-ports/luna-next-cardshell.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_prefix}/luna-next/shells/card"
