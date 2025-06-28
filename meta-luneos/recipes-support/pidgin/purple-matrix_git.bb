SUMMARY = "Matrix protocol plug-in for libpurple"
SECTION = "webos/services"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2c1c00f9d3ed9e24fa69b932b7e7aff2"

DEPENDS = "pidgin json-glib glib-2.0 zlib http-parser"

inherit pkgconfig

SRC_URI = "git://github.com/matrix-org/purple-matrix;branch=master;protocol=https"
SRCREV = "2792d4db0916ad821886de1f8de3a5e46c66ca37"

PV = "0.1.0+git"

do_compile() {
    oe_runmake CC="${CC}" MATRIX_NO_E2E=1;
}

do_install() {
    oe_runmake CC="${CC}" MATRIX_NO_E2E=1 DESTDIR="${D}" install;
}

FILES:${PN} += " \
    ${libdir} \
"
