SUMMARY = "Photos & Videos app re-written from scratch in Enyo 2"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_application

PV = "0.0.1+gitr${SRCPV}"
SRCREV = "25f39925b035f2d9f0b54e54b978e370f504b9c6"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
