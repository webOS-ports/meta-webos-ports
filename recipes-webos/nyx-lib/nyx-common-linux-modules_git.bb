DESCRIPTION = "Common linux modules for nyx-lib"
SECTION = "libs"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "nyx-lib"

SRCREV = "a1e3d95bd88b4b2ac5ab4f60c5bd1e1ec422c3bc"
SRC_URI = "git://github.com/openwebos-ports/nyx-common-linux-modules;protocol=git;branch=master"
S = "${WORKDIR}/git"

inherit autotools
