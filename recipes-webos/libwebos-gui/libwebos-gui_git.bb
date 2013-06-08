SUMMARY = "webOS graphics library"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS = "glib-2.0 libhybris"

PV = "1.0.0+gitr${SRCPV}"

inherit autotools

SRCREV = "bfed85b4ad44b924b17de7bc9be75e9280826390"
SRC_URI = "git://github.com/webOS-ports/${PN};branch=master;protocol=git"
S = "${WORKDIR}/git"
