SUMMARY = "Card shell implementation for the next generation webOS UI"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=88f260738df8472f0e269b664c5bb556"

RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtgraphicaleffects-qmlplugins \
    qtquickcontrols-qmlplugins \
    luna-next \
"

SRC_URI = "git://github.com/webOS-ports/luna-next-cardshell.git;branch=master;protocol=git"
SRCREV = "0047be7c4c69b3e62fc7fece0e92cd4ba0533bcf"
S = "${WORKDIR}/git"

# For the sake of the webOS build system we need to provide the webOS component version
# and even a submission number, even if we don't use any.
WEBOS_COMPONENT_VERSION = "0.1.0"
WEBOS_SUBMISSION = "0"
PV = "${WEBOS_COMPONENT_VERSION}+gitr${SRCPV}"

inherit webos_component
inherit webos_cmake

FILES_${PN} += "${webos_prefix}/luna-next/shells/card"
