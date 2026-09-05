SUMMARY = "LuneOS display manager (display states, ALS, suspend blocking)"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 json-c luna-sysmgr-common luna-sysmgr-ipc luna-sysmgr-ipc-messages luna-prefs nyx-lib libpbnjson pmloglib qtbase qtsensors"
RDEPENDS:${PN} += "sleepd com.webos.service.battery luna-authmanager"

PV = "1.0.0+git"
SRCREV = "2eb6e961c0e9e8fe4dd2cd2ef0d6486c48c793a0"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

inherit webos_ports_repo
inherit pkgconfig
inherit webos_system_bus
inherit webos_cmake_qt6
inherit webos_systemd
inherit webos_filesystem_paths

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

FILES:${PN} += "${webos_sysconfdir}"
