SUMMARY = "Card shell implementation for the next generation webOS UI"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7ea018afdcdcdd0dcc83d638cccf8b9"

RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtgraphicaleffects-qmlplugins \
    qtquickcontrols-qmlplugins \
    luna-next \
"

SRC_URI = "git://github.com/webOS-ports/luna-next-cardshell.git;branch=master;protocol=git"
SRCREV = "76b670fd69f84e58c43940a0b2feb0c1cd982008"
S = "${WORKDIR}/git"

# For the sake of the webOS build system we need to provide the webOS component version
# and even a submission number, even if we don't use any.
WEBOS_COMPONENT_VERSION = "0.1.0"
WEBOS_SUBMISSION = "0"
PV = "${WEBOS_COMPONENT_VERSION}+gitr${SRCPV}"

inherit webos_component
inherit webos_cmake

FILES_${PN} += "${webos_prefix}/luna-next/shells/card"
