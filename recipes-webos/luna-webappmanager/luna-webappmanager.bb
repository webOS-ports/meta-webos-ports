SUMMARY = "This is the web application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=0f93d2cf04b94ac3f04a789a1fb11ead \
    file://COPYING-Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57 \
"

DEPENDS = "qtbase qtdeclarative qtwebkit luna-sysmgr-common libconnman-qt"
RDEPENDS_${PN} += " \
    qtdeclarative-qmlplugins \
    qtwebkit-qmlplugins \
"

WEBOS_VERSION = "0.2.0-4_19330dfe0bf843bf92121fe9db842cbe6b007d55"

SRC_URI = "git://github.com/webOS-ports/luna-webappmanager.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_daemon
inherit webos_system_bus

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "LunaWebAppManager.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/LunaWebAppManager.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${webos_frameworksdir}"
