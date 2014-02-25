DESCRIPTION = "Wakelock handling daemon for proper suspend/resume integration"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "glib-2.0 libevdev libsuspend"

SRCREV = "60223e00c85199c1f23d6a41cb3fb4423fe24309"
SRC_URI = "git://github.com/webOS-ports/wakelockd;protocol=git;branch=master"
S = "${WORKDIR}/git"

PV = "0.1.0+gitr${SRCPV}"

inherit autotools
