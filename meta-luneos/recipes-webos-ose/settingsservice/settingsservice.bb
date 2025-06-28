# Copyright (c) 2013-2025 LG Electronics, Inc.

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

WEBOS_VERSION = "1.0.22-22_176fed142cb4a6417b5eaf4ca7cde0a114b1b1b4"
PR = "r26"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-service-update-SettingsService-path.patch \
    file://0002-com.webos.settingsservice-Change-luna.internal-to-se.patch \
"

#FIXME Remove service folder to avoid duplicate legacy and ACG role files
do_configure:append() {
    rm -rf ${S}/service
}

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "settings-service.service settings-service-recovery.service"
WEBOS_SYSTEMD_SCRIPT = "settings-service.sh"

WEBOS_SYSTEM_BUS_MANIFEST_TYPE = "SERVICE"

VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS:${PN}:append:class-target = " ${VIRTUAL-RUNTIME_bash}"
