SUMMARY = "LuneOS Location service"
SECTION = "webos/services"
LICENSE = "GPLv3+ & Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891 \
	file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

DEPENDS = "luna-service2 glib-2.0 libpbnjson"
RDEPENDS_${PN} = "geoclue"

PV = "0.1.0+git${SRCPV}"
WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
SRCREV = "c7dab24c81534b1047152edf09dee763b7294b5e"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "org.webosports.service.location.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/org.webosports.service.location.service ${D}${systemd_unitdir}/system/
}
