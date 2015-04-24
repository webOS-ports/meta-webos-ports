SUMMARY = "Lumberjack is an easy tool for viewing logfiles for a specific application."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"
DEPENDS = "luna-service2 glib-2.0 json-c"
PV = "0.1.0+git${SRCPV}"
SRCREV = "7a76ae617f1eec6385be19b5867ea1bfb6cdc4da"
inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd
WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
