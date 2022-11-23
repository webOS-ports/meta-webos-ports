# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Creates the database schema for webOS apps"
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 glib-2.0 pmloglib"

PV = "3.0.0-1+git${SRCPV}"
SRCREV = "f4f00f23b74df6e373cd27db75d951c3b4b9bf65"

WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg-new"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit webos_machine_impl_dep
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "configurator-activity.service configurator-db8.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/configurator-activity.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/configurator-db8.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} += "${systemd_unitdir}/system"

FILES:${PN} += "${webos_sysbus_datadir}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
