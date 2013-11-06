SECTION = "libs"
DESCRIPTION = "Presage is an intelligent predictive text entry system."
LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "sqlite3 libtinyxml"

inherit autotools gettext

SRC_URI = " \
    http://downloads.sourceforge.net/presage/presage-${PV}.tar.gz \
    file://disable-help2man.patch;striplevel=0 \
"
S = "${WORKDIR}/presage-${PV}"

SRC_URI[md5sum] = "13221794c5f55f2fde5c5938c59c8548"
SRC_URI[sha256sum] = "5541e9b350cc603a8d412704dcfa21342369b5b07c6da91947c7523c51678cd0"

EXTRA_OECONF = " \
    --disable-documentation \
    --disable-gpresagemate \
    --disable-gprompter \
    --disable-sqlite \
    --disable-python-binding \
"
