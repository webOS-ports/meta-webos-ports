# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Cython extension module for gbinder"
HOMEPAGE = "https://github.com/waydroid/gbinder-python"
LICENSE = "GPL-3.0-only"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

PV = "1.1.2+git"
SRCREV = "a2c5093c734e59df9f592010020cf081dadce81f"
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

# ERROR: python3-gbinder-1.1.2+git-r0 do_package_qa: QA Issue: File /usr/src/debug/python3-gbinder/1.1.2+git/gbinder.c in package python3-gbinder-src contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
