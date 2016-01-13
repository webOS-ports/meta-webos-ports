# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Sleep scheduling policy daemon"
AUTHOR = "Sapna Todwal <sapna.todwal@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c libxml2 sqlite3 glib-2.0 powerd"

PV = "1.1.1-30+git${SRCPV}"
SRCREV = "efa78e554d35da4157e822d49729e7bbde385115"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
	# Add the empty alarms.xml file to /var/preferences/com.palm.sleep to avoid unnecessary warning messages
	install -d ${D}${webos_preferencesdir}/com.palm.sleep
	install -v -m 644 files/conf/alarms.xml ${D}${webos_preferencesdir}/com.palm.sleep
}
