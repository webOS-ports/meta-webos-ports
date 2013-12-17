SUMMARY = "Card shell implementation for the next generation webOS UI"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=5340dda5a5599015e82b9796589180cc"

RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtgraphicaleffects-qmlplugins \
    qtquickcontrols-qmlplugins \
    luna-next \
"

SRC_URI = "git://github.com/webOS-ports/luna-next-cardshell.git;branch=master;protocol=git"
SRCREV = "cb4fc0602a137369a1cbb17245f6afc060795db9"
S = "${WORKDIR}/git"

# For the sake of the webOS build system we need to provide the webOS component version
# and even a submission number, even if we don't use any.
WEBOS_COMPONENT_VERSION = "0.1.0"
WEBOS_SUBMISSION = "0"
PV = "${WEBOS_COMPONENT_VERSION}+gitr${SRCPV}"

inherit webos_component
inherit webos_cmake

FILES_${PN} += "${webos_prefix}/luna-next/shells/card"
