# Copyright (c) 2017-2018 LG Electronics, Inc.

SUMMARY = "Application Install Service"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib pmtrace boost icu"
RDEPENDS:${PN} = "applicationinstallerutility ecryptfs-utils librolegen"

PV = "1.0.0-39+git${SRCPV}"
SRCREV = "0a5f7a37e5cbebca24593abeda86850a9fffdb95"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit webos_systemd
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-appinstalld-Add-systemd-service-file.patch \
file://0002-Correctly-handle-ipk-URIs.patch \
file://0003-AppInstaller-rescan-apps-after-install.patch \
file://0004-AppPackage-allow-different-owner-UIDs-GIDs.patch \
file://0005-appinstalld2-Make-org.webosports-privileged-as-well.patch \
"

S = "${WORKDIR}/git"

#FILES:${PN} += " \
#    ${systemd_unitdir}/system \
#"
