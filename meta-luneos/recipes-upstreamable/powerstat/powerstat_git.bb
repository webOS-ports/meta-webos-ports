SUMMARY = "Powerstat measures the power consumption of a laptop using the ACPI battery \
information."
HOMEPAGE = "http://kernel.ubuntu.com/~cking/powerstat/"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV = "0.03.03+git"
SRCREV = "2b075c9481b9045a7615b05fe46eb8581e2ee9d0"
SRC_URI = "git://github.com/ColinIanKing/${BPN}.git;protocol=https;branch=master \
    file://0001-Fix-build-with-glibc-2.38.patch \
"
S = "${WORKDIR}/git"

inherit bash-completion

EXTRA_OEMAKE = "MAKEFLAGS="

do_compile() {
    oe_runmake CC="${CC}"
}

do_install() {
    oe_runmake DESTDIR=${D} install
}
