SUMMARY = "webOS graphics library"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS = "glib-2.0 libhybris"

PV = "1.0.0+gitr${SRCPV}"
SRCREV = "bfed85b4ad44b924b17de7bc9be75e9280826390"

inherit autotools
inherit webos_ports_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
