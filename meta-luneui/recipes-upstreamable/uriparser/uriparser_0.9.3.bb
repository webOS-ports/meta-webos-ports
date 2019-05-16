# Copyright (c) 2012-2014 LG Electronics, Inc.

SECTION = "uriparser"
DESCRIPTION = "RFC 3986 compliant URI parsing library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=fc3bbde670fc6e95392a0e23bf57bda0"

PV = "0.9.3+git${SRCPV}"

SRCREV = "737e95f67bc2e5d8b90a1392797b353b52e5124a"
SRC_URI = "git://github.com/uriparser/${BPN}.git"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE += "-DURIPARSER_BUILD_DOCS:BOOL=OFF -DURIPARSER_BUILD_TESTS:BOOL=OFF"

SRC_URI[md5sum] = "9aabdc3611546f553f4af372167de6d6"
SRC_URI[sha256sum] = "ce7ccda4136974889231e8426a785e7578e66a6283009cfd13f1b24a5e657b23"
