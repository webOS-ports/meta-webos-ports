# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>
# Copyright (c) 2019 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "A header-only dbus-binding leveraging C++-11."
SECTION = "webos/support"
LICENSE = "GPL-2.0-or-later & LGPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://COPYING.GPL;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://COPYING.LGPL;md5=e6a600fd5e1d9cbde2d983680233ad02 \
    "

DEPENDS += "libxml2 properties-cpp process-cpp boost dbus"
RDEPENDS:${PN} += "boost-system dbus-lib boost-program-options boost-filesystem"

PV = "5.0.0+git"

SRCREV = "967dc1caf0efe0a1286c308e8e8dd1bf7da5f3ee"
SRC_URI = "git://github.com/lib-cpp/${BPN}.git;branch=master;protocol=https"

S = "${WORKDIR}/git"

EXTRA_OECMAKE += "-DDBUS_CPP_VERSION_MAJOR=5 -DDBUS_CPP_VERSION_MINOR=0 -DDBUS_CPP_VERSION_PATCH=0"

inherit cmake pkgconfig

do_configure:prepend() {
    echo " " > ${S}/tests/CMakeLists.txt
}

SRC_URI += "file://0001-boost-asio-1-66-build-issue-patch.patch"

# Until it's fixed properly to work with gcc9
CXXFLAGS += "-Wno-error=deprecated-copy"
