SUMMARY = "Powerstat measures the power consumption of a laptop using the ACPI battery \
information."
HOMEPAGE = "http://kernel.ubuntu.com/~cking/powerstat/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "http://kernel.ubuntu.com/~cking/tarballs/${BPN}/${BP}.tar.gz"
SRC_URI[md5sum] = "0da1ac7b54f72010aa3e869a3f722153"
SRC_URI[sha256sum] = "a5be7df819c1d60edbaabde13ef09c72b1bc1375ae3cad3444f0c084abf399be"

EXTRA_OEMAKE = "MAKEFLAGS="

do_compile() {
    oe_runmake CC="${CC}"
}

do_install() {
    oe_runmake DESTDIR=${D} install
}
