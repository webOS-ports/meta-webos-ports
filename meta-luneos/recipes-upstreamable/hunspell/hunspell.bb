# Copyright (c) 2012-2013 LG Electronics, Inc.
# Copyright (c) 2016 Herman van Hazendonk <github.com@herrie.org>

SECTION = "libs"
DESCRIPTION = "Hunspell spell checker and morphological analyzer"
LICENSE = "LGPL-2.1 & MPL-1.1"

LIC_FILES_CHKSUM = "file://COPYING.LGPL;md5=d8045f3b8f929c1cb29a1e3fd737b499 \
    file://COPYING.MPL;md5=bfe1f75d606912a4111c90743d6c7325 \
"

inherit autotools-brokensep gettext

SRCREV = "d7cead13301da710b6188bdac37310c05caee42c"

SRC_URI = "git://github.com/hunspell/hunspell.git;protocol=git;branch=1-4"
S = "${WORKDIR}/git"

PACKAGES =+ "${PN}-perl"
RDEPENDS_${PN}-perl = "perl"
FILES_${PN}-perl = "${bindir}/ispellaff2myspell"
