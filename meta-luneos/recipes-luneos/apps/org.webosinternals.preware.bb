SUMMARY = "Preware is a webOS on-device homebrew installer."
SECTION = "webos/apps"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit allarch
inherit webos_enyojs_application

PV = "2.0.2+gitr${SRCPV}"
SRCREV = "43d7f3d00eaa4acd048000727091f931aced4e60"

WEBOS_REPO_NAME = "preware"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
