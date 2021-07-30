# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "Loadable Node.js module for webOS services"
AUTHOR = "Sergiy Kryvonos <sergiy.kryvonos@lge.com>"
SECTION = "webOS/frameworks"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0.1-2+git${SRCPV}"
SRCREV = "7eeeca0180fbef26beae81f23239999feac88eda"

inherit webos_system_bus
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${libdir}/node_modules
    install -d ${D}${libdir}/node_modules/webos-service
    install -d ${D}${webos_sysbus_prvservicesdir}
    install -d ${D}${webos_sysbus_pubservicesdir}
    install -d ${D}${webos_sysbus_prvrolesdir}
    install -d ${D}${webos_sysbus_pubrolesdir}
    install -d ${D}${webos_servicesdir}
    for SUB in package.json lib ; do
        cp -R --no-dereference --preserve=mode,links -v $SUB ${D}${libdir}/node_modules/webos-service
    done
    for SUB in sample/* ; do
        cp -R --no-dereference --preserve=mode,links -v $SUB ${D}${webos_servicesdir}
    done
    # remove test and jasminetest dirs
    rm -vrf ${D}${libdir}/node_modules/webos-service/*test
}

PACKAGES =+ "${PN}-samples"
FILES:${PN}-samples += "${webos_servicesdir}"
FILES:${PN}-samples += "${webos_sysbus_prvservicesdir}"
FILES:${PN}-samples += "${webos_sysbus_pubservicesdir}"
FILES:${PN}-samples += "${webos_sysbus_prvrolesdir}"
FILES:${PN}-samples += "${webos_sysbus_pubrolesdir}"
FILES:${PN}-samples += "${webos_sysbus_manifestsdir}"
FILES:${PN} += "${libdir}/node_modules/webos-service/package.json"
FILES:${PN} += "${libdir}/node_modules/webos-service/lib"
