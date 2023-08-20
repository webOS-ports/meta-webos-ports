# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Power policy daemon"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c glib-2.0"

PV = "4.0.0-25+git${SRCPV}"
SRCREV = "765d2fa18a7899ac8e7d41e844914eb3485c06cd"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/powerd/4.0.0-25+gitAUTOINC+bbb74058dc-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: powerd/CMakeFiles/powerd.dir/charging/charging_logic.c.o: multiple definition of 'battery_ctia_params'
# /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/powerd/4.0.0-25+gitAUTOINC+bbb74058dc-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: powerd/CMakeFiles/powerd.dir/charging/battery.c.o: previous definition here
CFLAGS += "-fcommon"
