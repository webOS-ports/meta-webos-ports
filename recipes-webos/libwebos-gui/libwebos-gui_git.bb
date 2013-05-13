SUMMARY = "webOS graphics library"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS = "glib-2.0 libhybris"

PV = "1.0.0+gitr${SRCPV}"
PR = "r0"

inherit autotools

SRCREV = "82829bc553b29f643bbe9e82cb3285f96fc5b33d"
SRC_URI = "git://github.com/webOS-ports/${PN};branch=master;protocol=git"
S = "${WORKDIR}/git"
