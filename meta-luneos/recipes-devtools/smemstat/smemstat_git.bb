SUMMARY = "Smemstat reports the physical memory usage taking into consideration shared memory."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

# 0.01.10 was a plain terminal program; the curses UI arrived later and the Makefile
# now includes ncurses.h and links -lncursesw.
DEPENDS = "ncurses"

# 0.02.13 also installs a bash-completion script, which 0.01.10 did not.
inherit bash-completion

BASE_PV = "0.02.13"
PV = "${BASE_PV}+git"

# Upstream moved. kernel.ubuntu.com no longer serves this -- the host does not answer on the git
# daemon port at all, so the fetch hangs until it times out rather than failing cleanly, and the
# git:// protocol it used is retired anyway. Colin King's own GitHub mirror is the live home.
#
SRC_URI = "git://github.com/ColinIanKing/smemstat.git;branch=master;protocol=https"
S = "${WORKDIR}/git"

SRCREV = "1edc560602aa116b96408110b8b9d66a4edef60e"

do_compile() {
    oe_runmake CFLAGS="-DVERSION='\"${BASE_PV}\"'" smemstat
}

do_install() {
    oe_runmake DESTDIR=${D} install
}
