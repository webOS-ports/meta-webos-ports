SUMMARY = "Linux camera framework"
HOMEPAGE = "http://www.libcamera.org"
SECTION = "libs"

LICENSE = "GPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "\
    file://licenses/gnu-gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
    file://licenses/gnu-lgpl-2.1.txt;md5=4b54a1fd55a448865a0b32d41598759d \
"

DEPENDS = "udev qtbase qtbase-native"

PV = "0.1.0+git${SRCPV}"

SRCREV = "86194fdf4887dec547fcaaa71dcb7318e850d7ad"
SRC_URI = "git://gitlab.com/MartijnBraam/libcamera.git;protocol=https;branch=feature/pinephone"

S = "${WORKDIR}/git"

inherit meson pkgconfig

EXTRA_OEMESON += "-Ddocumentation=false -Dtest=false"

# Specify manually the content of -dev package as libcamera.so should be in libcamera main package
FILES:${PN}-dev = "${includedir} ${libdir}/pkgconfig"
FILES:${PN} += " ${libdir}/libcamera.so "
