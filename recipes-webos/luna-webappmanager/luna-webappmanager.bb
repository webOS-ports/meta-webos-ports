SUMMARY = "This is the web application launcher for the Luna Next webOS UI platform."
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=0f93d2cf04b94ac3f04a789a1fb11ead \
    file://COPYING-Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57 \
"

DEPENDS = "qtbase qtdeclarative qtwebkit luna-sysmgr-common"

SRC_URI = "git://github.com/webOS-ports/luna-webappmanager.git;branch=master;protocol=git"
SRCREV = "99c8684dee863b12f27f2d4edb8220f5f3fe285a"
S = "${WORKDIR}/git"

# For the sake of the webOS build system we need to provide the webOS component version
# and even a submission number, even if we don't use any.
WEBOS_COMPONENT_VERSION = "0.1.0"
WEBOS_SUBMISSION = "0"
PV = "${WEBOS_COMPONENT_VERSION}+gitr${SRCPV}"

inherit webos_component
inherit webos_daemon
inherit webos_system_bus

# We need to warrant the correct order for the following two inherits as webos_cmake is
# setting the build dir to be outside of the source dir which is overriden by cmake_qt5
# again if we inherit it afterwards.
inherit cmake_qt5
inherit webos_cmake

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "luna-webappmanager.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/luna-webappmanager.service ${D}${systemd_unitdir}/system/
}

FILES_${PN} += "${webos_frameworksdir}"
