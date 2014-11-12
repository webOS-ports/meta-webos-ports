# Copyright (c) 2010-2013 LG Electronics, Inc.

SUMMARY = "webOS ports Application Manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = " \
    cjson luna-service2 sqlite3 luna-sysmgr-ipc luna-sysmgr-ipc-messages \
    pmloglib librolegen nyx-lib openssl luna-webkit-api luna-prefs \
    libpbnjson freetype luna-sysmgr-common \
    qtbase qtquick1 serviceinstaller \
"

RDEPENDS_${PN} += " \
    bash \
"

WEBOS_VERSION = "1.0.0-18_db483e2773abdaaa24efafd4df45a27f3c25ba1c"

inherit webos_public_repo
inherit webos_daemon
inherit webos_enhanced_submissions
inherit webos_system_bus

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

SRC_URI = "git://github.com/webOS-ports/luna-appmanager.git;protocol=git;branch=master"
S = "${WORKDIR}/git"
