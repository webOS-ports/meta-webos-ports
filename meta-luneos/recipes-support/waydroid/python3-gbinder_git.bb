# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Cython extension module for gbinder"
HOMEPAGE = "https://github.com/waydroid/gbinder-python"
LICENSE = "GPL-3.0"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRCREV = "cd7620aa0d2e8875785708ad4263808636f3dce5"
SRC_URI = "git://github.com/waydroid/gbinder-python.git;branch=bullseye;protocol=https"

S = "${WORKDIR}/git"

DEPENDS = "libgbinder python3-cython-native"

RDEPENDS:${PN}:class-native = ""
DEPENDS:append:class-native = " python-native "

DISTUTILS_BUILD_ARGS = "sdist --cython"

inherit setuptools3

BBCLASSEXTEND = "native"

