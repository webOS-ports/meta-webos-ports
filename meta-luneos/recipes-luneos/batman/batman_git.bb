SUMMARY = "libbatman, the screen-on/off power tuning engine from FuriLabs batman"
DESCRIPTION = "FuriLabs batman's tuning engine as a shared library: CPU, GPU and \
devfreq governors, Android power HAL hints, vendor tuning knobs and process \
niceness. luna-batman drives it on LuneOS."
HOMEPAGE = "https://github.com/FuriLabs/batman"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "glib-2.0 libgbinder libnl"

SRC_URI = "git://github.com/FuriLabs/batman.git;branch=forky;protocol=https"
SRCREV = "53a107d3015be05ee90bddda61d5e178c69fdfaa"
PV = "1.0+git"
PR = "r5"

inherit pkgconfig

# The Makefile takes CFLAGS/LDFLAGS from the environment; CC and the
# install layout go on the command line because it sets CC itself.
EXTRA_OEMAKE = "CC='${CC}' PREFIX='${prefix}' LIBDIR='${libdir}' INCLUDEDIR='${includedir}'"

COMPATIBLE_MACHINE = "(halium)"

# Library only: the FuriOS daemon needs upower, PulseAudio and wayland.
do_compile() {
    oe_runmake lib
}

do_install() {
    oe_runmake install-lib DESTDIR='${D}'
    # luna-batman ships /etc/batman/config; libbatman still reads nice.conf
    # from its state directory.
    install -d ${D}/var/lib/batman
    install -m 0644 ${S}/data/nice.conf ${D}/var/lib/batman/nice.conf
}

PACKAGES =+ "libbatman"
FILES:libbatman = "${libdir}/libbatman.so.* /var/lib/batman"
