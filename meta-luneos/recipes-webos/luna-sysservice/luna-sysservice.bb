# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Provides preference, timezone and ringtone services"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

VIRTUAL-RUNTIME_ntp ?= "sntp"

DEPENDS = "luna-service2 libpbnjson qtbase uriparser libxml2 sqlite3 pmloglib nyx-lib libwebosi18n"

RDEPENDS_${PN} += "${VIRTUAL-RUNTIME_ntp} tzcode"

inherit webos_ports_repo
inherit webos_cmake
inherit webos_system_bus
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO}/${PN}-1;branch=webosose"
S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "luna-sys-service.service"

SRCREV = "170ea54cc60ebc55aa0549e74d25ee6552edc0b0"

do_install_append() {
    install -d ${D}${datadir}/localization/${BPN}
    cp -rf ${S}/resources ${D}/${datadir}/localization/${BPN}
    rm -rf ${D}${webos_sysbus_prvrolesdir}/com.webos.*
    rm -rf ${D}${webos_sysbus_pubrolesdir}/com.webos.* 

    install -d ${D}${systemd_unitdir}/system	
    install -m 0644 ${S}/files/systemd/${SYSTEMD_SERVICE_${PN}} ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${datadir}/localization/${BPN}"
