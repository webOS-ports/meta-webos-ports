SUMMARY = "libwebos-application implements lifecycle support for native applications."
LICENSE = "LGPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

DEPENDS += "luna-service2"

WEBOS_VERSION = "0.1.0-3_da907470c061be078684508bc0a845ceba59a00d"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"
