# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Cython extension module for gbinder"
HOMEPAGE = "https://github.com/waydroid/gbinder-python"
LICENSE = "GPL-3.0-only"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

# We're stuck @ 1.1.1 untill we are at cython3, build breaks with https://github.com/waydroid/gbinder-python/commit/4d8cb8f56da9e8159ea1b2ef76ddfa0253563db7
PV = "1.1.1+git"
SRCREV = "990c3007eeac3e015fb38aecd76dd010b4b75a1e"
SRC_URI = "git://github.com/waydroid/gbinder-python.git;branch=bullseye;protocol=https \
           file://0001-setup.py-Migrate-away-from-deprecated-distutils.core.patch \
"

S = "${WORKDIR}/git"

DEPENDS = "libgbinder python3-cython-native libglibutil"

RDEPENDS:${PN}:class-native = ""
DEPENDS:append:class-native = " python-native "

SETUPTOOLS_BUILD_ARGS = "sdist --cython"

inherit setuptools3 pkgconfig

BBCLASSEXTEND = "native"

