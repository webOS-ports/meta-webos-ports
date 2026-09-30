SUMMARY = "LuneOS display manager (display states, ALS, suspend blocking)"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 json-c luna-sysmgr-common luna-prefs nyx-lib libpbnjson pmloglib qtbase qtsensors"
RDEPENDS:${PN} += "sleepd com.webos.service.battery luna-authmanager"

PV = "1.0.0+git"
PR = "r11"

SRCREV = "c699dd79856b0d530fd4aaa817e2fef9563b4afa"

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

# A new method on an already-registered service needs ls-hubd to re-read the
# permissions files AND the service to re-register its category, in that order -
# otherwise the call returns "Denied method call", which looks exactly like a
# missing ACG while the ACG files are correct. Live devices only; an offline
# rootfs reads these at first boot anyway.
pkg_postinst:${PN}() {
    if [ -n "$D" ]; then
        exit 0
    fi

    if command -v ls-control > /dev/null 2>&1; then
        ls-control scan-services || true
    fi

    if systemctl is-active --quiet ${PN}.service; then
        systemctl restart ${PN}.service || true
    fi
}
