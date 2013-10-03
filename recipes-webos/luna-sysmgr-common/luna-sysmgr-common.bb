# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Library containing common parts of luna-sysmgr and webappmanager"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-prefs luna-service2 cjson nyx-lib libpbnjson sqlite3 pmloglib librolegen serviceinstaller"
DEPENDS += "luna-webkit-api"
DEPENDS += "luna-sysmgr-ipc luna-sysmgr-ipc-messages"
DEPENDS += "qtbase"

# temporary until we have oe-core with this patch included
# http://lists.openembedded.org/pipermail/openembedded-core/2013-July/080893.html
DEPENDS += "virtual/${TARGET_PREFIX}binutils"

WEBOS_VERSION = "3.0.0-3_00754405740b7f9d08ae0897f490b00123e17c2c"

# Don't uncomment until all of the do_*() tasks have been moved out of the recipe
#inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_library

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "c6c6d9d7e453f820caa1da3cf1570f345e89548a"

OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"
