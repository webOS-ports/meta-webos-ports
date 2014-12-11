SUMMARY = "Powerstat measures the power consumption of a laptop using the ACPI battery \
information."
HOMEPAGE = "http://kernel.ubuntu.com/~cking/powerstat/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://powerstat.c;beginline=1;endline=18;md5=1dceb0bc0ff50e42dbc9164e1c99de17"

SRC_URI = "http://kernel.ubuntu.com/~cking/tarballs/${BPN}/${BP}.tar.gz"
SRC_URI[md5sum] = "1264baf2e06fb16a5aee11294b043174"
SRC_URI[sha256sum] = "73967d673fa40597bdcf3e903c9dc203c1982d31b411b3e590395194bf710178"

EXTRA_OEMAKE = "MAKEFLAGS="

do_compile() {
    oe_runmake CC="${CC}"
}

do_install() {
    oe_runmake DESTDIR=${D} install
}
