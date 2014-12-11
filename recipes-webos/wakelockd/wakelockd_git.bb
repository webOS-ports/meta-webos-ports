DESCRIPTION = "Wakelock handling daemon for proper suspend/resume integration"
SECTION = "libs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "glib-2.0 libevdev libsuspend luna-service2 libpbnjson"

WEBOS_VERSION = "0.1.0-1_a3ac3ca8c3a36fed58243826b8289cac65339394"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_enhanced_submissions

SRC_URI = "git://github.com/webOS-ports/wakelockd;protocol=git;branch=master"
S = "${WORKDIR}/git"
