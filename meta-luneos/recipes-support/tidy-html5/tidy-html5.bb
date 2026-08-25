SUMMARY = "Tidy corrects and cleans up HTML content by fixing markup errors."
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://README/LICENSE.md;md5=fca2fcc0c318cb66d3871f8906117b17"

PV = "5.8.0"
SRCREV = "1ca37471b48a3498f985509828cb3cf85ea129f8"

# Was pinned to a 2012 commit of the pre-htacg tree. Upstream development moved to
# htacg/tidy-html5; w3c/tidy-html5 is a mirror whose master is this same 5.8.0 commit.
#
# 5.8.0 dropped the GNU autotools build entirely (only build/cmake remains), so this
# builds with cmake now and 0001-Correctly-deploy-automake-files-at-the-right-place.patch
# is gone along with the NEWS/ChangeLog/AUTHORS/README stubs autoreconf needed.
SRC_URI = "git://github.com/w3c/tidy-html5;branch=master;protocol=https"

inherit cmake pkgconfig

# LIB_INSTALL_DIR is a bare "lib${LIB_SUFFIX}" relative to the prefix upstream, so point
# it at ${baselib} to stay correct on lib64 targets. tab2space and the sample code are
# OFF by default upstream and nothing here wants them.
#
# TIDY_COMPAT_HEADERS defaults to OFF upstream, which drops buffio.h and platform.h
# from the installed set. imlibpurpleservice does #include <buffio.h> in
# src/sanitize.cpp, so without this it fails do_compile with "buffio.h: No such file
# or directory". The 2012 autotools build installed them unconditionally, so turning
# this on keeps the header set we had before. buffio.h is just a shim that includes
# tidybuffio.h, so consumers can migrate at their own pace.
EXTRA_OECMAKE = " \
    -DLIB_INSTALL_DIR=${baselib} \
    -DBUILD_SHARED_LIB=ON \
    -DSUPPORT_CONSOLE_APP=ON \
    -DTIDY_COMPAT_HEADERS=ON \
"

# Upstream still declares cmake_minimum_required(VERSION < 3.5), which CMake 4
# rejects. This recipe inherits plain cmake, not webos_cmake, so it needs the
# flag inline.
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
