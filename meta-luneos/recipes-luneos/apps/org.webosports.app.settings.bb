SUMMARY = "Settings app written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_app

PV = "0.3.0-1+git${SRCPV}"
SRCREV = "794850d2849af2b4da45a34de02e906a76670345"

WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg-js"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
