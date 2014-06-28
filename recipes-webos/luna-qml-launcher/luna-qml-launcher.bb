SUMMARY = "This is the qml application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=4ddd17b0c9241d7b24a4960caefe8e40 \
"

DEPENDS = "qtbase qtdeclarative luna-sysmgr-common libwebos-application"
RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    libconnman-qt \
"

WEBOS_VERSION = "0.1.0-0_eff512338ff4e5ee59ca9c5d902b44a1e2102bcd"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=tofe/work"
S = "${WORKDIR}/git"

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

FILES_${PN} += "${webos_frameworksdir}"
