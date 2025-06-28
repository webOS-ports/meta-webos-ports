SUMMARY = "Lumberjack is an easy tool for viewing logfiles for a specific application."
DESCRIPTION = "This is the service component needed for the lumberjack application"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 json-c"

PV = "0.1.0+git"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

WEBOS_REPO_NAME = "lumberjack"
SRCREV = "8af6f257319133c4bbee63c5f8aa168e9018136a"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/lumberjack.dir/src/luna_methods.c.o: multiple definition of 'priv_serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/lumberjack.dir/src/lumberjack.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/lumberjack.dir/src/luna_methods.c.o: multiple definition of 'pub_serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/lumberjack.dir/src/lumberjack.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/lumberjack.dir/src/luna_methods.c.o: multiple definition of 'serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/lumberjack.dir/src/lumberjack.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/lumberjack.dir/src/luna_service.c.o: multiple definition of 'serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/lumberjack.dir/src/lumberjack.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/lumberjack.dir/src/luna_service.c.o: multiple definition of 'pub_serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/lumberjack.dir/src/lumberjack.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/lumberjack.dir/src/luna_service.c.o: multiple definition of 'priv_serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.lumberjack/0.1.0+gitAUTOINC+aca9c3422c-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/lumberjack.dir/src/lumberjack.c.o: previous definition here
CFLAGS += "-fcommon"
