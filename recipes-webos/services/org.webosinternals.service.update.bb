SUMMARY = "Update management service for Open webOS"
SECTION = "webos/services"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 libpbnjson opkg"

PR = "r0"

inherit webos_component
inherit webos_public_repo
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
inherit webos_submissions

WEBOS_GIT_TAG = "${WEBOS_SUBMISSION}"
SRC_URI = "git://github.com/webOS-ports/${PN};tag=${WEBOS_GIT_TAG};protocol=git"
S = "${WORKDIR}/git"
