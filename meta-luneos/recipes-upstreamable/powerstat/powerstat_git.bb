SUMMARY = "Powerstat measures the power consumption of a laptop using the ACPI battery \
information."
HOMEPAGE = "http://kernel.ubuntu.com/~cking/powerstat/"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV = "0.04.06+git"
SRCREV = "84428aa8ed9e3b9c2abedc95b2633aa28395dac3"
# 0001-Fix-build-with-glibc-2.38.patch was dropped at V0.04.06: upstream renamed its
# private strlcpy/strlcat to bsd_strlcpy/bsd_strlcat, which resolves the collision
# with the versions glibc 2.38 gained.
SRC_URI = "git://github.com/ColinIanKing/${BPN}.git;protocol=https;branch=master"

inherit bash-completion

EXTRA_OEMAKE = "MAKEFLAGS="

do_compile() {
    oe_runmake CC="${CC}"
}

do_install() {
    oe_runmake DESTDIR=${D} install

    # V0.04.06 changed the bash-completion install from "cp" to "cp -p", which
    # preserves the build user's ownership from the source tree. pseudo then records
    # uid 1000 and do_package dies with "getpwuid(): uid not found: 1000".
    chown -R root:root ${D}
}
