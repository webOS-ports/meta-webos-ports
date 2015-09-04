SUMMARY = "webOS Ports First Use application"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

PV = "0.3.0-7+git${SRCPV}"
SRCREV = "e618fcccaed740f4dce8c52fd44d84368f7657ee"

DEPENDS += "qtbase qtdeclarative"

inherit webos_ports_repo
inherit webos_application
inherit webos_cmake_qt5
inherit webos_filesystem_paths

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

INSANE_SKIP_${PN} = "libdir"
INSANE_SKIP_${PN}-dbg = "libdir"

FILES_${PN} += "${datadir}/luneos-license-agreements"
FILES_${PN}-dbg += "${webos_applicationsdir}/${PN}/firstuse/.debug"
