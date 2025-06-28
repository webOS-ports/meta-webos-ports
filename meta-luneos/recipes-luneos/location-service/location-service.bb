SUMMARY = "LuneOS Location service"
SECTION = "webos/services"
LICENSE = "GPL-3.0-or-later & Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891 \
	file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

DEPENDS = "luna-service2 glib-2.0 libpbnjson"
RDEPENDS:${PN} = "geoclue"

PV = "0.1.0+git"
SRCREV = "948489ec5b4387e0ab9c5e5d49484ea49d440834"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "org.webosports.service.location.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/org.webosports.service.location.service ${D}${systemd_unitdir}/system/
}
