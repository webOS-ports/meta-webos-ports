SUMMARY = "Settings app written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qtbase qtdeclarative"

inherit webos_ports_repo
inherit webos_filesystem_paths

inherit cmake_qt5
inherit webos_cmake

PV = "0.4.0-1+git${SRCPV}"
SRCREV = "c77b88985ec334566559f75ad46caccf0679aa2e"

SRC_URI = "git://github.com/webOS-ports/org.webosports.app.settings.git;branch=qml-based"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.settings-common \
                ${webos_applicationsdir}/org.webosports.app.settings.wifi \
                ${webos_applicationsdir}/org.webosports.app.settings.bluetooth \
                ${webos_applicationsdir}/org.webosports.app.settings.about \
		${datadir}/ls2 \
		"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
"
