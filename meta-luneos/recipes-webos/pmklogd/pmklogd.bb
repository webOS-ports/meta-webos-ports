# Copyright (c) 2011-2019 LG Electronics, Inc.

SUMMARY = "Kernel logging daemon"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0"

PV = "2.0.0-1+git${SRCPV}"
SRCREV = "1c78e18ad5e60e83af7bc7bd3e19e08e17f724fb"

inherit webos_public_repo
inherit webos_cmake

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# http://caprica.lgsvl.com:8080/Errors/Details/1092094
# Configured/src/PmKLogDaemon.c:293:6: error: format not a string literal and no format arguments [-Werror=format-security]
#      fprintf(fp, gOutBuff+counter);
#      ^~~~~~~
SECURITY_STRINGFORMAT = ""