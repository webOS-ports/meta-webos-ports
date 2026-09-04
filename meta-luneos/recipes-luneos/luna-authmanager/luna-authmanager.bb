SUMMARY = "LuneOS authentication manager (device lock, EAS policy)"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 json-c qtbase"

PV = "1.0.0+git"
SRCREV = "8c32a7c48cdf3df1d07ad2a320d60f705ac9abb9"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

inherit webos_ports_repo
inherit pkgconfig
inherit webos_system_bus
inherit webos_cmake_qt6
inherit webos_systemd

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

# The com.palm.securitypolicy db8 kinds and permissions moved here from
# luna-sysmgr with the EAS policy engine that uses them.
do_install:append() {
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -v -m 644 ${S}/files/db/kinds/com.palm.securitypolicy ${D}${webos_sysconfdir}/db/kinds/
    install -v -m 644 ${S}/files/db/kinds/com.palm.securitypolicy.device ${D}${webos_sysconfdir}/db/kinds/
    install -d ${D}${webos_sysconfdir}/db/permissions
    install -v -m 644 ${S}/files/db/permissions/com.palm.securitypolicy ${D}${webos_sysconfdir}/db/permissions/
}

FILES:${PN} += "${webos_sysconfdir}"
