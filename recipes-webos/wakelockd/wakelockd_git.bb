DESCRIPTION = "Wakelock handling daemon for proper suspend/resume integration"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "glib-2.0"

SRCREV = "ec6d5386ea6ea0d398f9ff6a89e15a3d25813da4"
SRC_URI = "git://github.com/webOS-ports/wakelockd;protocol=git;branch=master"
S = "${WORKDIR}/git"

PV = "0.1.0+gitr${SRCPV}"

inherit autotools
