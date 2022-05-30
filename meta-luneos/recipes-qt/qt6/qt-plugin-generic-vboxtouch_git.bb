DESCRIPTION = "This driver extends Qt's platform support (QPA) for Virtualbox guests. \
It uses the integrated pointer feature to create a smooth conversion from \
the host pointer to touchscreen events in the guest, without grabbing the \
host pointer."
SUMMARY = "Touchscreen driver for integrated mouse pointer in VirtualBox"
LICENSE = "GPL-2.0-or-later | LGPL-3.0-only | The-Qt-Company-Commercial"
LIC_FILES_CHKSUM = " \
    file://vboxtouch.cpp;beginline=1;endline=22;md5=ca51db8f7c0606c77f702dcee4cf31d9 \
    file://evdevmousehandler.cpp;beginline=1;endline=38;md5=e6b661a57e804d0e9c4065e9ea275f33 \
"

PV = "1.1.4+gitr${SRCPV}"

DEPENDS = "qtbase"

# Needed with gcc-5.2 https://gcc.gnu.org/bugzilla/show_bug.cgi?id=65801
CXXFLAGS += "-Wno-narrowing"

inherit webos_ports_repo

WEBOS_REPO_NAME = "qt5-plugin-generic-vboxtouch"
WEBOS_GIT_PARAM_BRANCH = "herrie/qt6"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "80eafd2e54eeec3d0c9f58046613598b09473d20"
S = "${WORKDIR}/git/vboxtouch"

inherit qt6-qmake

FILES:${PN} += "${OE_QMAKE_PATH_PLUGINS}/generic/libvboxtouchplugin.so"
FILES:${PN}-dev += "${OE_QMAKE_PATH_LIBS}/cmake/*"

