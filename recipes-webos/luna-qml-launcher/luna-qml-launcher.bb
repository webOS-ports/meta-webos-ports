SUMMARY = "The qml application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40"

DEPENDS = "qtbase qtdeclarative luna-sysmgr-common libwebos-application"
RDEPENDS_${PN} += " qtdeclarative-qmlplugins"

PV = "0.1.0-5+git${SRCPV}"
SRCREV = "e3e00e960dfecd673ec593582bf0fd3ea4bfca8c"

inherit webos_ports_repo
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake
