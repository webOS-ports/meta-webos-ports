SUMMARY = "Card shell implementation for the next generation webOS UI"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=31ac144250416c44100353b586921adb"

RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtgraphicaleffects-qmlplugins \
    qtquickcontrols-qmlplugins \
    luna-next \
"

WEBOS_VERSION = "0.1.0-8_76b5a7c710f983dbf5badbc019d849677ced6eb3"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake

SRC_URI = "git://github.com/webOS-ports/luna-next-cardshell.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_prefix}/luna-next/shells/card"
