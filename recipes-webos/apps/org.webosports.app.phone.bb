SUMMARY = "Phone app written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative libwebos-application"
RDEPENDS_${PN} += "qtdeclarative-qmlplugins libqofono"

inherit webos_component
inherit webos_public_repo
inherit webos_system_bus
inherit webos_enhanced_submissions

WEBOS_VERSION = "0.1.0-1_43313348a8ba8f69aedb35f320b7686646697b85"

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git"
S = "${WORKDIR}/git"

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

FILES_${PN}-dbg += "${webos_applicationsdir}/${PN}/.debug"
FILES_${PN} += "${webos_applicationsdir}/${PN}"
