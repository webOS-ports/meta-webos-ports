# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "Settings Service"
AUTHOR = "Radhika S <radhika.s@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 glibmm luna-service2 libpbnjson pmloglib openssl libbson boost"
RDEPENDS_${PN} = "settingsservice-conf python"

PV = "1.0.22-1+git${SRCPV}"
inherit webos_cmake
inherit webos_system_bus
inherit webos_ports_repo
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "settings-service-recovery.service settings-service.service"

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

SRCREV = "e243ddfe472c236d0fa4b740e35fa4fc6bc071a5"

WEBOS_SYSTEM_BUS_MANIFEST_TYPE = "SERVICE"

VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS_${PN}_append_class-target = " ${VIRTUAL-RUNTIME_bash}"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/settings-service-recovery.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/settings-service.service ${D}${systemd_unitdir}/system/
}

