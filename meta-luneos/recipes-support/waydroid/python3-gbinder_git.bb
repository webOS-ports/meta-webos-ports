# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Cython extension module for gbinder"
HOMEPAGE = "https://github.com/waydroid/gbinder-python"
LICENSE = "GPL-3.0-only"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

PV = "1.0.0+git${SRCPV}"
SRCREV = "79d40e9e564772973f7f085ed5c48e3fc625e0f5"
SRC_URI = "git://github.com/waydroid/gbinder-python.git;branch=main;protocol=https"

S = "${WORKDIR}/git"

DEPENDS = "libgbinder python3-cython-native libglibutil"

RDEPENDS:${PN}:class-native = ""
DEPENDS:append:class-native = " python-native "

SETUPTOOLS_BUILD_ARGS = "sdist --cython"

inherit setuptools3 pkgconfig

BBCLASSEXTEND = "native"

