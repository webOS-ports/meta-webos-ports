# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "Sleep scheduling policy daemon"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "nyx-lib luna-service2 json-c libxml2 sqlite3 glib-2.0"
#Added for LuneOS
RDEPENDS:${PN} += "com.webos.service.battery"

SRCREV = "472951734cfbfa68e16ff87219d0faf65d15fe33"

PV = "2.0.0-20"

PR = "r20"

inherit webos_component
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
LUNEOS_SYSTEMD_SERVICE = "sleepd.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/sleep.conf.d
    install -m 0644 ${S}/files/systemd/sleep.conf.d/10-sleepd.conf \
        ${D}${systemd_unitdir}/sleep.conf.d/
}

FILES:${PN} += "${systemd_unitdir}/sleep.conf.d/10-sleepd.conf"
