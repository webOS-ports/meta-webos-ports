# Copyright (c) 2012-2013 LG Electronics, Inc.
# Copyright (c) 2023 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "Battery & charger daemon"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c glib-2.0"

PV = "1.0.0-1+git"
SRCREV = "ca1767800350b0b1a357988e6df3f6c7efe6660c"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

# /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/powerd/4.0.0-25+gitAUTOINC+bbb74058dc-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: powerd/CMakeFiles/powerd.dir/charging/charging_logic.c.o: multiple definition of 'battery_ctia_params'
# /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/powerd/4.0.0-25+gitAUTOINC+bbb74058dc-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: powerd/CMakeFiles/powerd.dir/charging/battery.c.o: previous definition here
CFLAGS += "-fcommon"

# com.webos.service.battery/1.0.0-1+git/git/batteryd/utils/uevent.c:88:9: error: too many arguments to function 'func'; expected 0, have 2
CFLAGS += "-std=gnu17"
