SUMMARY = "Extra modules and scripts for CMake"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING-CMAKE-SCRIPTS;md5=54c7042be62e169199200bc6477f04d1 \
    file://LICENSES/BSD-3-Clause.txt;md5=954f4d71a37096249f837652a7f586c0 \
"

PV = "6.28.0"
SRCREV = "01dc9a0c05dd4851b01b93e961c9aa33b1e96056"

# anongit.kde.org still redirects here, but KDE moved to invent.kde.org years ago
# so point at the real home rather than relying on the redirect.
SRC_URI = " \
    git://invent.kde.org/frameworks/extra-cmake-modules.git;branch=master;protocol=https \
    file://0001-FindQtWaylandScanner-Search-within-OE_QMAKE_PATH_EXT.patch \
"
S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DBUILD_TESTING=off"

inherit cmake

FILES:${PN}-dev += "${datadir}/ECM"

# ${PN} package is empty
RDEPENDS:${PN}-dev = ""
