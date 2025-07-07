SUMMARY = "Package management service for webOS/LuneOS"
SECTION = "webos/services"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 json-c"

PV = "2.0.0-2+git"
SRCREV = "cca6470dc7b5b8f31470dd269d013e782cd0b4b8"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

WEBOS_REPO_NAME = "preware"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/oe-service"

pkg_postinst:${PN}() {
    #!/bin/sh -e

    APPS=/media/cryptofs/apps

    # Create the opkg config and database areas
    mkdir -p $D$APPS/${sysconfdir}/opkg $D$APPS/${localstatedir}/lib/opkg/cache

    # We provide an empty status file to satisfy the ipkgservice
    touch $D$APPS/${localstatedir}/lib/opkg/status

    # Remove all list database cache files
    rm -f $D$APPS/${localstatedir}/lib/opkg/lists/*

    # Install webosports all-arch feeds
    echo "src/gz webosports http://feeds.webos-ports.org/webos-ports/all" > $D$APPS/${sysconfdir}/opkg/webos-ports.conf

    # Add additional feeds which are disabled by default and NOT SUPPORTED by webOS-ports
    # ports / LuneOS. The user has to turn them on manually to use them.
    echo "src PivotCE https://feed.pivotce.com" > $D$APPS/${sysconfdir}/opkg/pivotce.conf.disabled
    # echo "src Macaw-enyo https://minego.net/preware/macaw-enyo" > $D$APPS/${sysconfdir}/opkg/macaw-enyo.conf.disabled
    echo "src Hominid-Software https://hominidsoftware.com/preware" > $D$APPS/${sysconfdir}/opkg/hominid-software.conf.disabled
    echo "src/gz FeedSpider2 https://www.hunternet.ca/fs/luneos" > $D$APPS/${sysconfdir}/opkg/feedspider.conf.disabled
}

# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/org.webosports.service.ipkg.dir/src/luna_methods.c.o: multiple definition of 'priv_serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/org.webosports.service.ipkg.dir/src/ipkgservice.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/org.webosports.service.ipkg.dir/src/luna_methods.c.o: multiple definition of 'pub_serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/org.webosports.service.ipkg.dir/src/ipkgservice.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/org.webosports.service.ipkg.dir/src/luna_methods.c.o: multiple definition of 'serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/org.webosports.service.ipkg.dir/src/ipkgservice.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/org.webosports.service.ipkg.dir/src/luna_service.c.o: multiple definition of 'serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/org.webosports.service.ipkg.dir/src/ipkgservice.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/org.webosports.service.ipkg.dir/src/luna_service.c.o: multiple definition of 'pub_serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/org.webosports.service.ipkg.dir/src/ipkgservice.c.o: previous definition here
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: error: CMakeFiles/org.webosports.service.ipkg.dir/src/luna_service.c.o: multiple definition of 'priv_serviceHandle'
# | /OE/build/luneos-master/webos-ports/tmp-glibc/work/core2-64-webos-linux/org.webosports.service.ipkg/2.0.0-2+gitAUTOINC+2f5147149b-r0/recipe-sysroot-native/usr/bin/x86_64-webos-linux/../../libexec/x86_64-webos-linux/gcc/x86_64-webos-linux/10.2.0/ld: CMakeFiles/org.webosports.service.ipkg.dir/src/ipkgservice.c.o: previous definition here
CFLAGS += "-fcommon"
