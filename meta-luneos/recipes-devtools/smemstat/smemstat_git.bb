SUMMARY = "Smemstat reports the physical memory usage taking into consideration shared memory."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

BASE_PV = "0.01.10"
PV = "${BASE_PV}+git"

# Upstream moved. kernel.ubuntu.com no longer serves this -- the host does not answer on the git
# daemon port at all, so the fetch hangs until it times out rather than failing cleanly, and the
# git:// protocol it used is retired anyway. Colin King's own GitHub mirror is the live home.
#
# SRCREV is deliberately unchanged: 787d4de ("debian: update changelog", 2014-06-03) exists there
# and is an ancestor of master, so this fetches byte-identical source to what the old URL gave.
# Only where it comes from changed, not what is built.
SRC_URI = "git://github.com/ColinIanKing/smemstat.git;branch=master;protocol=https"
S = "${WORKDIR}/git"

SRCREV = "787d4de350ab492baf199a70075d5ccf7187a517"

do_compile() {
    oe_runmake CFLAGS="-DVERSION='\"${BASE_PV}\"'" smemstat
}

do_install() {
    oe_runmake DESTDIR=${D} install
}
