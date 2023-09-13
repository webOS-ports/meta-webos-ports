# Copyright (c) 2012-2023 LG Electronics, Inc.

SUMMARY = "Provides preference, timezone and ringtone services"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=7bd705f8ae3d5077cbd3da7078607d8b \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

VIRTUAL-RUNTIME_ntp ?= "sntp"

DEPENDS = "luna-service2 libpbnjson qtbase uriparser libxml2 sqlite3 pmloglib nyx-lib libwebosi18n"

RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_ntp} tzcode luna-init"

WEBOS_VERSION = "4.4.0-25_527dfeba2b7d1c7f84ea5e8775b1f1ec4c40b183"
PR = "r11"

PV = "4.4.0-25+git${SRCPV}"
SRCREV = "527dfeba2b7d1c7f84ea5e8775b1f1ec4c40b183"

inherit webos_public_repo
inherit webos_cmake_qt6
inherit webos_system_bus
inherit webos_configure_manifest
inherit systemd
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-Add-ImageService.patch \
file://0002-luna-sysservice-Fix-spacing-issues.patch \
file://0003-luna-sysservice-Add-required-bits-for-LuneOS.patch \
"

S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "luna-sys-service.service"

do_install:append() {
    install -d ${D}${datadir}/localization/${BPN}
    cp -rf ${S}/resources ${D}/${datadir}/localization/${BPN}
    # FIXME: We still need this or registration fails
    rm -rf ${D}${webos_sysbus_prvrolesdir}/com.webos.*
    rm -rf ${D}${webos_sysbus_pubrolesdir}/com.webos.* 
}

FILES:${PN} += "${datadir}/localization/${BPN}"
