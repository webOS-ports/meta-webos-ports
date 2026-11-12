SUMMARY = "LuneOS haptics (vibration) service"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 json-c nyx-lib"

PV = "1.0.0+git"
SRCREV = "318aa59df10f7f43344f7921ec11fe595f5d7323"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

inherit webos_ports_repo
inherit pkgconfig
inherit webos_system_bus
inherit webos_cmake
inherit webos_systemd

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
