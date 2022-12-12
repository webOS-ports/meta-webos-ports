# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Creates the database schema for webOS apps"
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 glib-2.0 pmloglib"

PV = "3.0.0-9+git${SRCPV}"
SRCREV = "657fb30f8754e42ac0e56151722c5b729b5bde71"

inherit webos_public_repo
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

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-configurator-Add-service-file-for-systemd-script.patch \
file://0002-configurator-Fix-permission-issue-for-com.palm.filec.patch \
file://0003-com.webos.service.configurator.perm.json-Add-permiss.patch \
file://0004-com.palm.configurator.role.json.in-Add-fixes-for-var.patch \
"

S = "${WORKDIR}/git"
