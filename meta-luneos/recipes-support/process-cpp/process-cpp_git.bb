# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>
# Copyright (c) 2019 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "A simple convenience library for handling processes in C++11."
SECTION = "webos/support"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02 \
    "

DEPENDS = "boost properties-cpp"
RDEPENDS_${PN} += "boost-system boost-iostreams"

PV = "3.0.1+git${SRCPV}"

SRCREV = "2923b597f6fc5b49133be8c4f2ba3cbaacdb9540"
SRC_URI = "git://github.com/lib-cpp/${BPN}.git"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE += " -DPROCESS_CPP_ENABLE_DOC_GENERATION=OFF"

do_configure_prepend() {
  echo " " > ${S}/tests/CMakeLists.txt
}
