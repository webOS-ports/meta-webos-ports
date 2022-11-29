# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Provides preference, timezone and ringtone services"
AUTHOR = "Keith Derrick <keith.derrick@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

VIRTUAL-RUNTIME_ntp ?= "sntp"

DEPENDS = "luna-service2 libpbnjson qtbase uriparser libxml2 sqlite3 pmloglib nyx-lib libwebosi18n"

RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_ntp} tzcode"

inherit webos_public_repo
inherit webos_cmake_qt6
inherit webos_system_bus
inherit webos_configure_manifest
inherit systemd
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-luna-sysservice-Add-service-file-for-systemd.patch \
file://0002-Add-ImageService.patch \
file://0003-luna-sysservice-Fix-spacing-issues.patch \
file://0004-luna-sysservice-Add-required-bits-for-LuneOS.patch \
"

S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "luna-sys-service.service"

PV = "4.4.0-23+git${SRCPV}"
SRCREV = "c346f6ed15a860780776b74d08080cfa57c9c578"

do_install:append() {
    install -d ${D}${datadir}/localization/${BPN}
    cp -rf ${S}/resources ${D}/${datadir}/localization/${BPN}
    rm -rf ${D}${webos_sysbus_prvrolesdir}/com.webos.*
    rm -rf ${D}${webos_sysbus_pubrolesdir}/com.webos.* 

    install -d ${D}${systemd_unitdir}/system	
    install -m 0644 ${S}/files/systemd/${SYSTEMD_SERVICE:${PN}} ${D}${systemd_unitdir}/system/
}

FILES:${PN} += "${datadir}/localization/${BPN}"
