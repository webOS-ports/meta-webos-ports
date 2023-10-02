SUMMARY = "Package management service for webOS/LuneOS"
SECTION = "webos/services"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 json-c"

PV = "2.0.0-2+git"
SRCREV = "ee2ab46cf128390fd36fde108c417e2ec8c50552"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

WEBOS_REPO_NAME = "preware"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git/oe-service"

pkg_postinst_ontarget:${PN}() {
    #!/bin/sh -e

    APPS=/media/cryptofs/apps

    # Create the opkg config and database areas
    mkdir -p $APPS/${sysconfdir}/opkg $APPS/${localstatedir}/lib/opkg/cache

    # We provide an empty status file to satisfy the ipkgservice
    touch $APPS/${localstatedir}/lib/opkg/status

    # Remove all list database cache files
    rm -f $APPS/${localstatedir}/lib/opkg/lists/*

    # Set up the architecture configuration file
    rm -f $APPS/${sysconfdir}/opkg/arch.conf
    cp ${sysconfdir}/opkg/arch.conf $APPS/${sysconfdir}/opkg/arch.conf

    # Install webosports all-arch feeds
    echo "src/gz webosports http://feeds.webos-ports.org/webos-ports/all" > $APPS/${sysconfdir}/opkg/webos-ports.conf

    # Add additional feeds which are disabled by default and NOT SUPPORTED by webOS-ports
    # ports / LuneOS. The user has to turn them on manually to use them.
    echo "src PivotCE https://feed.pivotce.com" > $APPS/${sysconfdir}/opkg/pivotce.conf.disabled
    echo "src Macaw-enyo https://minego.net/preware/macaw-enyo" > $APPS/${sysconfdir}/opkg/macaw-enyo.conf.disabled
    echo "src Hominid-Software https://hominidsoftware.com/preware" > $APPS/${sysconfdir}/opkg/hominid-software.conf.disabled
    echo "src/gz FeedSpider2 https://www.hunternet.ca/fs/luneos" > $APPS/${sysconfdir}/opkg/feedspider.conf.disabled
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
