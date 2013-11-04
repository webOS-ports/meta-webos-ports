# Copyright (c) 2012-2014 LG Electronics, Inc.

DESCRIPTION = "c-ares is a C library that resolves names asynchronously."
HOMEPAGE = "http://daniel.haxx.se/projects/c-ares/"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://README;beginline=17;endline=18;md5=b320556568bc067d215a1e34c5b34a14"

inherit autotools
inherit pkgconfig

SRC_URI = "http://c-ares.haxx.se/download/${BP}.tar.gz"
SRC_URI[md5sum] = "1196067641411a75d3cbebe074fd36d8"
SRC_URI[sha256sum] = "3d701674615d1158e56a59aaede7891f2dde3da0f46a6d3c684e0ae70f52d3db"

EXTRA_OECONF = "--enable-shared"

# install private headers to ares subdirectory
do_install_append() {
    install -d ${D}/${includedir}/ares
    install -m 0644 ares*.h ${D}/${includedir}/ares/
}

FILES_${PN}-dev += "${includedir}/ares/*.h"
