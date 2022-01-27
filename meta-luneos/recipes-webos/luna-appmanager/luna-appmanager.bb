# Copyright (c) 2010-2013 LG Electronics, Inc.

SUMMARY = "webOS ports Application Manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = " \
    json-c luna-service2 sqlite3 luna-sysmgr-ipc luna-sysmgr-ipc-messages \
    pmloglib librolegen nyx-lib openssl luna-webkit-api luna-prefs \
    libpbnjson freetype luna-sysmgr-common \
    qtbase qtdeclarative serviceinstaller \
"

RDEPENDS:${PN} += " \
    bash \
"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=herrie/appinstalld2"

SRCREV = "0a551e600fd9449fb2616fe9633d8c89902d4ef3"
PV = "1.0.0-22+git${SRCPV}"

S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

inherit webos_ports_repo
inherit pkgconfig
inherit webos_system_bus
inherit webos_cmake_qt5
inherit webos_systemd
