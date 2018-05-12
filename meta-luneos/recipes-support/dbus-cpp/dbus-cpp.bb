SUMMARY = "A header-only dbus-binding leveraging C++-11."
SECTION = "webos/support"
LICENSE = "GPLv2+ & LGPLv3"
LIC_FILES_CHKSUM = " \
    file://COPYING.GPL;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://COPYING.LGPL;md5=e6a600fd5e1d9cbde2d983680233ad02 \
    "

DEPENDS += "libxml2 properties-cpp process-cpp boost dbus"
RDEPENDS_${PN} += "boost-system dbus-lib boost-program-options boost-filesystem"

PV = "5.0.0+16.10.20160809"

SRC_URI = "http://archive.ubuntu.com/ubuntu/pool/universe/d/dbus-cpp/dbus-cpp_5.0.0+16.10.20160809.orig.tar.gz"
SRC_URI[md5sum] = "1ca6e981f4a53de0ca5dee6a314da7f6"

S = "${WORKDIR}"

EXTRA_OECMAKE += "-DDBUS_CPP_VERSION_MAJOR=5 -DDBUS_CPP_VERSION_MINOR=0 -DDBUS_CPP_VERSION_PATCH=0"

inherit cmake pkgconfig

do_configure_prepend() {
  echo " " > ${S}/tests/CMakeLists.txt
}
