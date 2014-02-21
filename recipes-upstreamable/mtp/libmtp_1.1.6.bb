# Copyright (c) 2014 LG Electronics, Inc.

SUMMARY = "MTP Library"
DESCRIPTION = "libmtp allows you to access smart phone or media player"
SECTION = "libs"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=0448d3676bc0de00406af227d341a4d1"
DEPENDS = "libusb libgcrypt"

SRC_URI = "http://sourceforge.net/projects/libmtp/files/libmtp/${PV}/libmtp-${PV}.tar.gz \
	file://09_do_not_build_udev_and_examples.patch \
"

SRC_URI[md5sum] = "87835626dbcf39e62bfcdd4ae6da2063"
SRC_URI[sha256sum] = "3a1c1c83af91de4052eb54feb795c141b4c04a252e0031954ebbf6175970cb0a"

inherit autotools binconfig pkgconfig

do_configure_append() {
    sed "/(noinst_DATA)/d" Makefile.in > Makefile_export.in
    sed "/mtp-hotplug/d" Makefile_export.in > Makefile_export2.in
    mv Makefile_export2.in Makefile.in
    rm -f Makefile_export.in
}

EXTRA_OECONF = "--disable-mtpz "
