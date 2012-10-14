DESCRIPTION = "Wakelock handling daemon for proper suspend/resume integration"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "glib-2.0"

SRCREV = "7ff6cccb7090577ef7e7d8132c301e32c69fb158"
SRC_URI = "git://github.com/webOS-ports/wakelockd;protocol=git;branch=master"
S = "${WORKDIR}/git"

PV = "0.1.0+gitr${SRCPV}"
PR = "r0"

inherit autotools
