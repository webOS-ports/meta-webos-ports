SUMMARY = "Preware is a webOS on-device homebrew installer."
SECTION = "webos/apps"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_ose_repo
inherit webos_filesystem_paths
inherit allarch
inherit webos_enyojs_application
inherit webos_app

PV = "2.0.3+git${SRCPV}"
SRCREV = "321186367c499761dca78de2028687a7124b12e4"

WEBOS_REPO_NAME = "preware"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
