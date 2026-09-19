SUMMARY = "LuneOS display manager (display states, ALS, suspend blocking)"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 json-c luna-sysmgr-common luna-prefs nyx-lib libpbnjson pmloglib qtbase qtsensors"
RDEPENDS:${PN} += "sleepd com.webos.service.battery luna-authmanager"

PV = "1.0.0+git"
PR = "r5"
SRCREV = "0f691947324171e0b96603eb5b1ef14c29bb211b"

# Set here rather than appended to SRC_URI: WEBOS_PORTS_GIT_REPO_COMPLETE
# already carries a ";branch=", which defaults to master through
# webos_public_repo, so a second one in SRC_URI left the fetcher with two.
WEBOS_GIT_PARAM_BRANCH = "herrie/display-policy"

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
