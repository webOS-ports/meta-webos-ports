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
SRCREV = "47bcc6fb77f089f7ee6f3acfb8ef7086db0c9808"

SRC_URI = "git://github.com/webOS-ports/org.webosports.app.settings.git;branch=tofe/qml-work"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_applicationsdir}/org.webosports.app.settings-common \
                ${webos_applicationsdir}/org.webosports.app.settings.wifi \
                ${webos_applicationsdir}/org.webosports.app.settings.bluetooth \
		${datadir}/ls2 \
		"

RDEPENDS_${PN} = " \
    qtdeclarative-qmlplugins \
"
