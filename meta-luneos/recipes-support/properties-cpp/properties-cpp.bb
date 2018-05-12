SUMMARY = "A very simple convenience library for handling properties and signals in C++11."
SECTION = "webos/support"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02 \
    "

PV = "0.0.1+14.10.20140730"

SRC_URI = "http://archive.ubuntu.com/ubuntu/pool/universe/p/properties-cpp/properties-cpp_0.0.1+14.10.20140730.orig.tar.gz"
SRC_URI[md5sum] = "449f95cc864ebe38a35848885ca1cb4b"

do_configure_prepend() {
  echo " " > ${S}/tests/CMakeLists.txt
}

inherit cmake pkgconfig

