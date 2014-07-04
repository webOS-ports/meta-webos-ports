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

WEBOS_VERSION = "0.1.0-21_d8a06194e54ed0c1e437bda25a817c20315cef59"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_tweaks

SRC_URI = "git://github.com/webOS-ports/luna-next-cardshell.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_prefix}/luna-next/shells/card"
