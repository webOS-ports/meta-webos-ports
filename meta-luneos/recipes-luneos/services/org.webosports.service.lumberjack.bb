SUMMARY = "Lumberjack is an easy tool for viewing logfiles for a specific application."
DESCRIPTION = "This is the service component needed for the lumberjack application"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 json-c"

PV = "0.1.0+git${SRCPV}"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_REPO_NAME = "lumberjack"
WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
SRCREV = "befef2f9701350430a7904a654fe5363d780ef17"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"


