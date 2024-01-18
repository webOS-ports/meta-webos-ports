SUMMARY = "Certificate Management Service"
SECTION = "webos/services"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "luna-service2 json-c glib-2.0 pmcertificatemgr"

PV = "0.1.0-1+git${SRCPV}"
SRCREV = "9000690778d574a46a0bb8e404394660145ea3bb"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "org.webosports.service.certmgr.service"


do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/org.webosports.service.certmgr.service ${D}${systemd_unitdir}/system/
}

