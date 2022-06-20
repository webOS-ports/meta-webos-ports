SUMMARY = "Extra modules and scripts for CMake"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING-CMAKE-SCRIPTS;md5=54c7042be62e169199200bc6477f04d1 \
    file://LICENSES/BSD-3-Clause.txt;md5=954f4d71a37096249f837652a7f586c0 \
"

PV = "5.95.0+git${SRCPV}"
SRCREV = "633e3793ca94f2aec8605a48bec2b27f4dbde366"

SRC_URI = " \
    git://anongit.kde.org/extra-cmake-modules;branch=master;protocol=https \
    file://0001-FindQtWaylandScanner-Search-within-OE_QMAKE_PATH_EXT.patch \
"
S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DBUILD_TESTING=off"

inherit cmake

FILES:${PN}-dev += "${datadir}/ECM"

# ${PN} package is empty
RDEPENDS:${PN}-dev = ""
