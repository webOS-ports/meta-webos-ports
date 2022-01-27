# Copyright (c) 2017-2018 LG Electronics, Inc.

SUMMARY = "Application Install Service"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib pmtrace boost icu"
RDEPENDS:${PN} = "applicationinstallerutility ecryptfs-utils librolegen"

PV = "1.0.0-2+git${SRCPV}"
SRCREV = "ac8d7e5c7f4ac9fbc6c132974c1dee8d1d09ecaf"

inherit webos_cmake
inherit webos_system_bus
inherit webos_ports_fork_repo
inherit systemd

WEBOS_GIT_PARAM_BRANCH = "herrie/rebased"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "appinstalld.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system	
    install -m 0644 ${S}/files/systemd/${SYSTEMD_SERVICE_${PN}} ${D}${systemd_unitdir}/system/
}

FILES:${PN} += " \
    ${systemd_unitdir}/system \
"
