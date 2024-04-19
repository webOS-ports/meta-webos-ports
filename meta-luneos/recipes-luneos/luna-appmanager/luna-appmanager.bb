# Copyright (c) 2010-2013 LG Electronics, Inc.

SUMMARY = "webOS ports Application Manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = " \
    json-c luna-service2 sqlite3 luna-sysmgr-ipc luna-sysmgr-ipc-messages \
    pmloglib librolegen nyx-lib openssl luna-prefs \
    libpbnjson freetype luna-sysmgr-common \
    qtbase qtdeclarative serviceinstaller \
"

RDEPENDS:${PN} += " \
    bash \
"

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

SRCREV = "8d7e7a51e2de63c65af9f89583b156b86f7a02c9"

PV = "1.0.0-22+git"

S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

inherit webos_ports_repo
inherit pkgconfig
inherit webos_system_bus
inherit webos_cmake_qt6
inherit webos_systemd
