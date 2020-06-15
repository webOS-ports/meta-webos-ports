SUMMARY = "Preware is a webOS on-device homebrew installer."
SECTION = "webos/apps"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit allarch
inherit webos_enyojs_application
inherit webos_app

PV = "2.0.3+git${SRCPV}"
SRCREV = "98501be35a5a522d33c5d1e5add7311673c754dd"

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
WEBOS_REPO_NAME = "preware"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
