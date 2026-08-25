# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Cython extension module for gbinder"
HOMEPAGE = "https://github.com/waydroid/gbinder-python"
LICENSE = "GPL-3.0-only"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

PV = "1.3.1"
SRCREV = "86b8feba4cacd0952b010d1c3af6a29a0c146ced"
# Upstream moved the packaging branch: 1.3.x is tagged on main, the old bullseye
# branch stopped at 1.1.2. The local distutils->setuptools patch is dropped here
# because upstream made the same change in 4a95a7d.
SRC_URI = "git://github.com/waydroid/gbinder-python.git;branch=main;protocol=https"

DEPENDS = "libgbinder python3-cython-native libglibutil"

RDEPENDS:${PN}:class-native = ""
DEPENDS:append:class-native = " python-native "

# The --cython option is gone: upstream's "setup: Always build with cython"
# (38ed307) made setup.py cythonize() unconditionally, so passing it now fails with
# "option --cython not recognized".
inherit setuptools3 pkgconfig

BBCLASSEXTEND = "native"

# ERROR: python3-gbinder-1.1.2+git-r0 do_package_qa: QA Issue: File /usr/src/debug/python3-gbinder/1.1.2+git/gbinder.c in package python3-gbinder-src contains reference to TMPDIR [buildpaths]
# Scoped to -src: only the generated source carries the build path, and that
# package never reaches an image. Demoting the check for the whole recipe
# would also stop it catching a build path in something that does ship.
INSANE_SKIP:${PN}-src += "buildpaths"
