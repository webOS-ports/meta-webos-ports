# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Creates the database schema for Open webOS apps"
AUTHOR = "Ludovic Legrand <ludovic.legrand@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 db8 json-c glib-2.0 pmloglib"

PV = "3.0.0-59+git${SRCPV}"
SRCREV = "45fb8458100a90fd7700a759c10836ead4760e20"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit webos_machine_impl_dep
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "configurator.service configurator-async.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/configurator.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/configurator-async.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${systemd_unitdir}/system"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

CXXFLAGS += "-fpermissive"
