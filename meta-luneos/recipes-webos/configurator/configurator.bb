# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "Creates the database schema for webOS apps"
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 glib-2.0 pmloglib"

PV = "3.0.0-1+git${SRCPV}"
SRCREV = "e4c3665099a345b5c437219b7c7550baa68071e2"

inherit webos_ports_repo
inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus
#inherit pkgconfig
inherit webos_machine_impl_dep
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "configurator-activity.service configurator-db8.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/configurator-activity.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/configurator-db8.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${systemd_unitdir}/system"

FILES_${PN} += "${webos_sysbus_datadir}"

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
