FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
DESCRIPTION = "System specific changes for the Qt Quick Scene Graph."
LICENSE = "LGPL-2.1-only & GPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE.GPL;md5=d32239bcb673463ab874e80d47fae504 \
    file://LICENSE.LGPL;md5=4193e7f1d47a858f6b7c0f1ee66161de \
"

PV = "6.3.0+git${SRCPV}"
SRCREV = "e146d14d80157b5a37fe9c012259b1c610fde5fe"

DEPENDS = "qtbase libhybris qtwayland virtual/android-headers qtdeclarative"

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

WEBOS_GIT_PARAM_BRANCH = "herrie/qt6"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit qt6-qmake webos_ports_fork_repo

EXTRA_QMAKEVARS_PRE += "CONFIG+=surfaceformat CONFIG+=programbinary CONFIG+=hybristexture"

FILES:${PN} += "${OE_QMAKE_PATH_PLUGINS}"
