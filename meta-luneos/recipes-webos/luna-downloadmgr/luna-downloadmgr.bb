# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY="The Download Manager service supports the downloading and uploading of files to and from a HP webOS device."
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libpbnjson luna-service2 sqlite3 curl uriparser pmloglib jemalloc luna-prefs boost glib-2.0"

#For LuneOS luna-appmanage now provides the same as applicationinstallerutility by the looks of it. So for now we'll depend on that and change into applicationinstallerutility as soon as we migrate away from luna-appmanager
#RDEPENDS_${PN} = "applicationinstallerutility"
RDEPENDS_${PN} = "luna-appmanager"

PV = "4.0.0-1+git${SRCPV}"
SRCREV = "47c7c3cc9e425fed4da71e62e7f6e6c08a197f7f"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo

WEBOS_MACHINE ?= "${MACHINE}"
EXTRA_OECMAKE += "-DMACHINE=${WEBOS_MACHINE}"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
