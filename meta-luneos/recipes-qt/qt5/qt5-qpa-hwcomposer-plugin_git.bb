DESCRIPTION = "This QPA plugin allows rendering on top of libhybris-based hwcomposer EGL \
platforms. The hwcomposer API is specific to a given Droid release, and \
sometimes also SoC type (generic, qcom, exynos4, ...)."
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://hwcomposer_backend.cpp;beginline=1;endline=40;md5=09c08382077db2dbc01b1b5536ec6665"

PV = "5.6.0+git${SRCPV}"
SRCREV = "df9bddc316a30ee46055c700a40d68bb73e7e87b"

DEPENDS = "qtbase libhybris qtwayland virtual/android-headers"

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

SRC_URI = " \
    git://github.com/mer-hybris/qt5-qpa-hwcomposer-plugin.git \
    file://0002-Fix-4.4.2-hwcomposer-build.patch;patchdir=.. \
    file://0003-Qt-5.8-support.patch;patchdir=.. \
    file://0004-Fix-hwcomposer-backend.patch;patchdir=.. \
    file://0005-hwcomposer_backend_v11-fix-compatibility-with-qtbase.patch;patchdir=.. \
    file://0006-Revert-Add-systraces-to-v11.patch;patchdir=.. \
    file://0007-qeglfsintegration-stop-using-deprecated-and-in-5.13-.patch;patchdir=.. \
"
S = "${WORKDIR}/git/hwcomposer"

inherit webos_ports_fork_repo
inherit qmake5

# WARNING: The recipe qt5-qpa-hwcomposer-plugin is trying to install files into a shared area when those files already exist. Those files and their manifest location are:
#   /OE/build/owpb/webos-ports/tmp-eglibc/sysroots/tenderloin/usr/lib/cmake/Qt5Gui/Qt5Gui_QEglFSIntegrationPlugin.cmake
#   Matched in manifest-tenderloin-qtbase.populate_sysroot
#   Please verify which package should provide the above files.
do_install_append() {
    rm -vf ${D}${libdir}/cmake/Qt5Gui/Qt5Gui_QEglFSIntegrationPlugin.cmake
}

FILES_${PN} += "${OE_QMAKE_PATH_PLUGINS}/platforms/libhwcomposer.so"
FILES_${PN}-dev += "${libdir}/cmake"
