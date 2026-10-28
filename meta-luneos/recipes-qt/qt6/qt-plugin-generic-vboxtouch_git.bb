DESCRIPTION = "This driver extends Qt's platform support (QPA) for Virtualbox guests. \
It uses the integrated pointer feature to create a smooth conversion from \
the host pointer to touchscreen events in the guest, without grabbing the \
host pointer."
SUMMARY = "Touchscreen driver for integrated mouse pointer in VirtualBox"
LICENSE = "GPL-2.0-or-later | LGPL-3.0-only | The-Qt-Company-Commercial"
LIC_FILES_CHKSUM = " \
    file://vboxtouch.cpp;beginline=1;endline=23;md5=ad0ca8d736c54a63306324171717ead4 \
    file://evdevmousehandler.cpp;beginline=1;endline=38;md5=e6b661a57e804d0e9c4065e9ea275f33 \
"

PV = "1.1.4+git"

DEPENDS = "qtbase qtdeclarative"

# Needed with gcc-5.2 https://gcc.gnu.org/bugzilla/show_bug.cgi?id=65801
CXXFLAGS += "-Wno-narrowing"

inherit webos_ports_repo

WEBOS_REPO_NAME = "qt5-plugin-generic-vboxtouch"
# Testing only: the evdev mode for guests without vboxguest is on
# herrie/qt6-evdev-mode in Herrie82's fork, not yet in webOS-ports; back to
# webOS-ports master once it is.
WEBOS_PORTS_GIT_REPO = "git://github.com/Herrie82"
WEBOS_GIT_PARAM_BRANCH = "herrie/qt6-evdev-mode"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
SRCREV = "772df208f14a54ff5eedbb5e37d9cee1e76415f3"
S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}/vboxtouch"

inherit qt6-qmake

FILES:${PN} += "${OE_QMAKE_PATH_PLUGINS}/generic/libvboxtouchplugin.so"
FILES:${PN}-dev += "${OE_QMAKE_PATH_LIBS}/cmake/*"

