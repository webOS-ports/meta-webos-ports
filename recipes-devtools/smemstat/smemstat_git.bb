SUMMARY = "Smemstat reports the physical memory usage taking into consideration shared memory."
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

BASE_PV = "0.01.10"
PV = "${BASE_PV}+gitr${SRCPV}"

SRC_URI = "git://kernel.ubuntu.com/cking/smemstat.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

SRCREV = "787d4de350ab492baf199a70075d5ccf7187a517"

do_compile() {
    oe_runmake CFLAGS="-DVERSION='\"${BASE_PV}\"'" smemstat
}

do_install() {
    oe_runmake DESTDIR=${D}
}
