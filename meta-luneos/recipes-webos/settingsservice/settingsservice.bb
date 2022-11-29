# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "Settings Service"
AUTHOR = "Radhika S <radhika.s@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 glibmm luna-service2 libpbnjson pmloglib openssl libbson boost"
RDEPENDS:${PN} = "settingsservice-conf python3"

PV = "1.0.22-17+git${SRCPV}"
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

SRCREV = "29ff049918d5b9e48afea58d421f0bc56fbd9852"

WEBOS_SYSTEM_BUS_MANIFEST_TYPE = "SERVICE"

VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS:${PN}:append:class-target = " ${VIRTUAL-RUNTIME_bash}"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/settings-service-recovery.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/settings-service.service ${D}${systemd_unitdir}/system/
}

