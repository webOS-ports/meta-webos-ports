# Copyright (c) 2012-2024 LG Electronics, Inc.

DESCRIPTION = "webOS component to manage all running activities."
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/dameons"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "luna-service2 db8 boost libpbnjson glib-2.0 pmloglib ${VIRTUAL-RUNTIME_init_manager}"

SRCREV = "bf16f54a85e5577bc755b3cc6deb9a7f047bb274"
PV = "3.0.0-45"
PR = "r19"

inherit webos_component
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_daemon

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "activitymanager.service"

FILES:${PN} += "${webos_sysbus_datadir}"
FILES:${PN} += "${localstatedir}/lib/activitymanager"

EXTRA_OECMAKE += "-DINIT_MANAGER:STRING='${@bb.utils.filter('VIRTUAL-RUNTIME_init_manager', 'systemd upstart', d)}'"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'webos-dac', d)}"
PACKAGECONFIG[webos-dac] = "-DDAC_IMPLEMENTATION:BOOL=TRUE,,"

do_install:append() {
        install -m 0700 -o system -g system -v -d ${D}${localstatedir}/lib/activitymanager
}
