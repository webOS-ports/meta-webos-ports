SUMMARY = "LuneOS Location service"
SECTION = "webos/services"
LICENSE = "GPLv3+ & Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891 \
	file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"

DEPENDS = "luna-service2 glib-2.0 libpbnjson"
RDEPENDS_${PN} = "geoclue"

PV = "0.1.0+git${SRCPV}"
SRCREV = "53453e944a06fc5dde633a8727dd4edf2ff573df"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus
inherit webos_systemd

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
