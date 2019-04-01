SUMMARY = "A very simple convenience library for handling properties and signals in C++11."
SECTION = "webos/support"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

PV = "0.0.1+14.10.20140730"

SRC_URI = "http://archive.ubuntu.com/ubuntu/pool/universe/p/properties-cpp/properties-cpp_0.0.1+14.10.20140730.orig.tar.gz"
SRC_URI[md5sum] = "449f95cc864ebe38a35848885ca1cb4b"
SRC_URI[sha256sum] = "5c5400d5e7f16e90916d9be2742785c68e7ce544641739a380d2d815cff67223"

do_configure_prepend() {
    echo " " > ${S}/tests/CMakeLists.txt
}

inherit cmake pkgconfig

RDEPENDS_${PN}-dev = ""
