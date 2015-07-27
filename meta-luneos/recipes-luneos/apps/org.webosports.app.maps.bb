SUMMARY = "Google Maps app re-written from scratch in Enyo 2"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_application

PV = "0.0.1+gitr${SRCPV}"
SRCREV = "a37e3132cc663f09d04766a4b10092c858b9d6c0"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
