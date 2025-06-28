FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
DESCRIPTION = "System specific changes for the Qt Quick Scene Graph."
LICENSE = "LGPL-2.1-only & GPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE.GPL;md5=d32239bcb673463ab874e80d47fae504 \
    file://LICENSE.LGPL;md5=4193e7f1d47a858f6b7c0f1ee66161de \
"

PV = "0.8.0+git"
SRCREV = "7ecfeaa23e2038a761bd1dfa95726dc0eed536d1"

DEPENDS = "qtbase libhybris qtwayland virtual/android-headers qtdeclarative"

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

SRC_URI = "git://github.com/sailfishos/qtscenegraph-adaptation.git;protocol=https;branch=master"

inherit qt6-qmake

EXTRA_QMAKEVARS_PRE += "CONFIG+=surfaceformat CONFIG+=programbinary CONFIG+=hybristexture"

FILES:${PN} += "${OE_QMAKE_PATH_PLUGINS}"
