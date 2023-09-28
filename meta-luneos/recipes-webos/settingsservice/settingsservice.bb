# Copyright (c) 2013-2023 LG Electronics, Inc.

SUMMARY = "Settings Service"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 glibmm luna-service2 libpbnjson pmloglib openssl libbson boost"
RDEPENDS:${PN} = "settingsservice-conf db8"

WEBOS_VERSION = "1.0.22-19_7684fcc6813104ee5c139701f8ed6627ef851b97"
PR = "r26"

PV = "1.0.22-19+git${SRCPV}"
SRCREV = "7684fcc6813104ee5c139701f8ed6627ef851b97"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit systemd
inherit pkgconfig

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "settings-service-recovery.service settings-service.service"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-settingsservice-Add-service-files-for-systemd-script.patch \
file://0002-service-update-SettingsService-path.patch \
"


S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_MANIFEST_TYPE = "SERVICE"

VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS:${PN}:append:class-target = " ${VIRTUAL-RUNTIME_bash}"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/settings-service-recovery.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/settings-service.service ${D}${systemd_unitdir}/system/
}

