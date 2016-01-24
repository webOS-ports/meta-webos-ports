SUMMARY = "PDF viewer application based on Mozilla's pdf.js"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths

PV = "1.0.0+gitr${SRCPV}"
SRCREV = "4e7d7fee56cc973198149b2b3c8e92cd5b83f422"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
