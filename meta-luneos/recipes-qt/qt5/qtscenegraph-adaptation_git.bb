DESCRIPTION = "System specific changes for the Qt Quick Scene Graph."
LICENSE = "LGPL-2.1 & GPL-3.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE.GPL;md5=d32239bcb673463ab874e80d47fae504 \
    file://LICENSE.LGPL;md5=4193e7f1d47a858f6b7c0f1ee66161de \
"

PV = "5.4.0+gitr${SRCPV}"
SRCREV = "9b30eef992067848e2e99b3662694f4d0667b831"

DEPENDS = "qtbase libhybris qtwayland virtual/android-headers"

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    git://github.com/mer-hybris/qtscenegraph-adaptation \
    file://0001-Link-against-libEGL-to-satisfy-runtime-dependencies.patch \
    file://0002-Fix-createTexture-API-for-Qt5-6.patch \
"
S = "${WORKDIR}/git"

require recipes-qt/qt5/qt5.inc

EXTRA_QMAKEVARS_PRE += "CONFIG+=surfaceformat CONFIG+=programbinary CONFIG+=hybristexture"

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}"
FILES_${PN}-dbg += "${OE_QMAKE_PATH_PLUGINS}/*/.debug"
