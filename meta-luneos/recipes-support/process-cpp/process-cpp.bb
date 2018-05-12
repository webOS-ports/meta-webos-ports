SUMMARY = "A simple convenience library for handling processes in C++11."
SECTION = "webos/support"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02 \
    "

DEPENDS = "boost properties-cpp"
RDEPENDS_${PN} += "boost-system boost-iostreams"

PV = "3.0.1"

SRC_URI = "http://archive.ubuntu.com/ubuntu/pool/universe/p/process-cpp/process-cpp_3.0.1.orig.tar.gz"
SRC_URI[md5sum] = "95e187de74037b70b105679f85047c12"

inherit cmake pkgconfig

EXTRA_OECMAKE += " -DPROCESS_CPP_ENABLE_DOC_GENERATION=OFF"

do_configure_prepend() {
  echo " " > ${S}/tests/CMakeLists.txt
}
