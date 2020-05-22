SUMMARY = "Google Maps app re-written from scratch in Enyo 2"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_application
inherit webos_app

PV = "0.0.1+git${SRCPV}"
SRCREV = "7c3d0a1f41c3e9c3449bc96ef924fd707136c05d"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
