SUMMARY = "webOS Ports system update application"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_public_repo
inherit webos_arch_indep
inherit webos_enyojs_application

PV = "0.1.0+gitr${SRCPV}"

SRCREV = "4c418620b6c64d5ac9195873472213088371da7d"
SRC_URI = "git://github.com/webOS-ports/org.webosports.update;protocol=git;branch=master"
S = "${WORKDIR}/git/app"
