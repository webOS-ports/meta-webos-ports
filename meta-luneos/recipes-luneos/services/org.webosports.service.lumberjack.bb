SUMMARY = "Lumberjack is an easy tool for viewing logfiles for a specific application."
DESCRIPTION = "This is the service component needed for the lumberjack application"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 json-c"

PV = "0.1.0+git${SRCPV}"
SRCREV = "ba62f198c8b06592954aeb720c47559d7b1d7215"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

WEBOS_REPO_NAME = "lumberjack"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
