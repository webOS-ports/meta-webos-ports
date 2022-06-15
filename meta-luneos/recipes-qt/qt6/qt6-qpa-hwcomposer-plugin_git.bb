DESCRIPTION = "This QPA plugin allows rendering on top of libhybris-based hwcomposer EGL \
platforms. The hwcomposer API is specific to a given Droid release, and \
sometimes also SoC type (generic, qcom, exynos4, ...)."
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://hwcomposer_backend.cpp;beginline=1;endline=40;md5=09c08382077db2dbc01b1b5536ec6665"

PV = "6.3.0+git${SRCPV}"
SRCREV = "34d4b40abe437d7e9201cfd8c9b6349bf85969d0"

DEPENDS = "qtbase libhybris qtwayland virtual/android-headers"

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

SRC_URI = " \
    git://github.com/herrie82/qt5-qpa-hwcomposer-plugin.git;branch=herrie/qt6;protocol=https \
"
S = "${WORKDIR}/git/hwcomposer"

inherit webos_ports_fork_repo
inherit qt6-qmake pkgconfig

# WARNING: The recipe qt5-qpa-hwcomposer-plugin is trying to install files into a shared area when those files already exist. Those files and their manifest location are:
#   /OE/build/owpb/webos-ports/tmp-eglibc/sysroots/tenderloin/usr/lib/cmake/Qt5Gui/Qt5Gui_QEglFSIntegrationPlugin.cmake
#   Matched in manifest-tenderloin-qtbase.populate_sysroot
#   Please verify which package should provide the above files.
do_install:append() {
    rm -vf ${D}${libdir}/cmake/Qt5Gui/Qt5Gui_QEglFSIntegrationPlugin.cmake
}

FILES:${PN} += "${OE_QMAKE_PATH_PLUGINS}/platforms/libhwcomposer.so"
FILES:${PN}-dev += "${libdir}/cmake"
