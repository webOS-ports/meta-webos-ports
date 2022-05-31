SUMMARY = "webOS Ports First Use application"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"

PV = "0.3.0-7+git${SRCPV}"
SRCREV = "25e480d2d825bd2cfa148f98699d1a0dd64c916d"

DEPENDS += "qtbase qtdeclarative"

inherit webos_ports_repo
inherit webos_application
inherit webos_cmake_qt6
inherit webos_filesystem_paths
inherit webos_app

WEBOS_GIT_PARAM_BRANCH = "wam"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += "${datadir}/luneos-license-agreements"
