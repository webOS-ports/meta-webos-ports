# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>
# Copyright (c) 2019 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "A simple convenience library for handling processes in C++11."
SECTION = "webos/support"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02 \
    "

DEPENDS = "boost properties-cpp"
RDEPENDS:${PN} += "boost-system boost-iostreams"

PV = "3.0.1+git"

SRCREV = "2923b597f6fc5b49133be8c4f2ba3cbaacdb9540"
SRC_URI = "git://github.com/lib-cpp/${BPN}.git;branch=master;protocol=https"

inherit cmake pkgconfig

EXTRA_OECMAKE += " -DPROCESS_CPP_ENABLE_DOC_GENERATION=OFF"

do_configure:prepend() {
  echo " " > ${S}/tests/CMakeLists.txt
}
