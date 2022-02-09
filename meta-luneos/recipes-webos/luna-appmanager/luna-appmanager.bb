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

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};branch=herrie/OSE-wam-honister-chromium87"

SRCREV = "f2cf4dda3c0b835f3d94d6b2a6f063944f319068"

PV = "1.0.0-22+git${SRCPV}"

S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""

inherit webos_ports_repo
inherit pkgconfig
inherit webos_system_bus
inherit webos_cmake_qt5
inherit webos_systemd
