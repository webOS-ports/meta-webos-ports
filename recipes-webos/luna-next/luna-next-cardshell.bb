SUMMARY = "Card shell implementation for the next generation webOS UI"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=88f260738df8472f0e269b664c5bb556"

RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtgraphicaleffects-qmlplugins \
    qtquickcontrols-qmlplugins \
    luna-next \
"

WEBOS_VERSION = "0.1.0-2_ad37cc260fbe798188a835d93ea95beb514aeea9"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake

SRC_URI = "git://github.com/webOS-ports/luna-next-cardshell.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_prefix}/luna-next/shells/card"
