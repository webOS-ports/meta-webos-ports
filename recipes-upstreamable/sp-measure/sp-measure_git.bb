# Copyright (c) 2013 LG Electronics, Inc.

SUMMARY = "A library for measuring resource consumption (CPU, memory)"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRCREV = "bb4ced89842f8d2dc0b8a6bc5d631bb36355207b"
PV = "1.3.5+git${SRCPV}"

inherit autotools

SRC_URI = "git://gitorious.org/maemo-tools/sp-measure.git \
           file://build-fixes-for-compiling-in-OE.patch \
           file://do-not-compile-doc-directory-if-doxygen-is-missing.patch"

S = "${WORKDIR}/git"
