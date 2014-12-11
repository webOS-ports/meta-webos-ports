SUMMARY = "Small library implementing Android suspend mechanism"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

WEBOS_VERSION = "1.0.0-2_838ef4eafcc9bb5231584ca58ce9b636e1003712"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit pkgconfig
inherit cmake

SRC_URI = "git://github.com/webOS-ports/libsuspend.git;branch=master;protocol=git"
S = "${WORKDIR}/git"
