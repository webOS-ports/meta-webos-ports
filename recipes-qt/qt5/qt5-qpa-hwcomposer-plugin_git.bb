DESCRIPTION = "This QPA plugin allows rendering on top of libhybris-based hwcomposer EGL \
platforms. The hwcomposer API is specific to a given Droid release, and \
sometimes also SoC type (generic, qcom, exynos4, ...)."
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://hwcomposer_backend.cpp;beginline=1;endline=40;md5=09c08382077db2dbc01b1b5536ec6665"

PV = "5.2.0+gitr${SRCPV}"

DEPENDS = "qtbase libhybris qtwayland"

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "git://github.com/webOS-ports/qt5-qpa-hwcomposer-plugin.git;branch=webOS-ports/master;protocol=git"
SRCREV = "864140fd8956ca8479c19f7ce6943f99e1fc1a66"
S = "${WORKDIR}/git/hwcomposer"

SRC_URI_append_tenderloin = " \
    file://0001-HACK-fix-support-for-HP-touchpad-with-hwcomposer-1.3.patch;striplevel=2 \
"

inherit qmake5

# Set path of qt5 headers as qmake5_base.bbclass sets this to just ${includedir} but
# actually it is ${includedir}/qt5
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

FILES_${PN} += "${libdir}/qt5/plugins/platforms/libhwcomposer.so"
FILES_${PN}-dbg += "${libdir}/qt5/plugins/platforms/.debug/libhwcomposer.so"
FILES_${PN}-dev += "${libdir}/cmake"
