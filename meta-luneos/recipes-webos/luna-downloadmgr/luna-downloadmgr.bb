# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY="The Download Manager service supports the downloading and uploading of files to and from a HP webOS device."
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libpbnjson luna-service2 sqlite3 curl uriparser pmloglib jemalloc luna-prefs boost glib-2.0"

RDEPENDS:${PN} = "applicationinstallerutility"

PV = "4.0.0-11+git${SRCPV}"
SRCREV = "1d5261104a9fbf01c6eb5f2572d3f33d274f3dd2"

inherit webos_cmake
inherit webos_system_bus
inherit webos_ports_ose_repo
inherit webos_systemd
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES:${PN} += " \
    ${systemd_unitdir}/system \
"
