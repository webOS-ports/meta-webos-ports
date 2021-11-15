SUMMARY = "Powerstat measures the power consumption of a laptop using the ACPI battery \
information."
HOMEPAGE = "http://kernel.ubuntu.com/~cking/powerstat/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV = "0.02.27+git${SRCPV}"
SRCREV = "556762740c6a6e19cf6842da926d184a51706ecb"
SRC_URI = "git://github.com/ColinIanKing/${BPN}.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

EXTRA_OEMAKE = "MAKEFLAGS="

do_compile() {
    oe_runmake CC="${CC}"
}

do_install() {
    oe_runmake DESTDIR=${D} install
}
