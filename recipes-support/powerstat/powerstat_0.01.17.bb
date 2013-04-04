SUMMARY = "Powerstat measures the power consumption of a laptop using the ACPI battery \
information."
HOMEPAGE = "http://kernel.ubuntu.com/~cking/powerstat/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://powerstat.c;beginline=1;endline=18;md5=2d43006c297e4b9372eea336e3d980b2"

SRC_URI = "http://kernel.ubuntu.com/~cking/tarballs/powerstat/powerstat-${PV}.tar.gz"
SRC_URI[md5sum] = "38e4e2eb43f58cd0dd1b92dfdd3d599a"
SRC_URI[sha256sum] = "feac9f83866b6687100759408ba5f5796dd4ad0def9773b642ec6a2dc00fb08a"

do_compile() {
    oe_runmake CC="${CC}"
}

do_install() {
    oe_runmake DESTDIR=${D} install
}
