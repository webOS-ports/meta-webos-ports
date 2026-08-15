SUMMARY = "Audiosystem Passthrough"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e9f185e65d260379da52afe6fc486efc"

DEPENDS += "libgbinder systemd libglibutil glib-2.0"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig

PV = "1.3.1+git"
SRCREV = "11a0d95f43c92580a42c08a050b2b5bf18e0f475"

SRC_URI = " \
        git://github.com/mer-hybris/audiosystem-passthrough.git;branch=master;protocol=https \
"

TARGET_CC_ARCH += "${LDFLAGS}"

# DEBUG_PREFIX_MAP has to ride along on CC: upstream's Makefile assigns CFLAGS
# itself, so OE's CFLAGS never reach the compiler and the debug info keeps
# absolute TMPDIR paths. That is only a warning on scarthgap but fails
# do_package_qa on wrynose with "contains reference to TMPDIR [buildpaths]".
EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CROSS_COMPILE=${TARGET_PREFIX} CC='${CC} ${DEBUG_PREFIX_MAP}' "
PARALLEL_MAKE = ""

do_install() {
    oe_runmake install DESTDIR=${D}
}
