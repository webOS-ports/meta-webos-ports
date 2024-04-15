SUMMARY = "Sensorfw plugin for qtsensors"
LICENSE = "LGPL-2.1-only | LGPL-3.0-only"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv21;md5=cff17b12416c896e10ae2c17a64252e7 \
    file://LICENSE.LGPLv3;md5=c1939be5579666be947371bc8120425f \
    file://LGPL_EXCEPTION.txt;md5=0145c4d1b6f96a661c2c139dfb268fb6 \
"

DEPENDS = "qtbase qtsensors sensorfw"
RDEPENDS:${PN} = "sensorfw bash"

# We're depending on sensorfw so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
# COMPATIBLE_MACHINE = "^halium$"

PV = "6.5.2+git"
SRCREV = "4fbf12f43e4ba63cbac8a24805e2549fa67bd772"

inherit webos_ports_repo
inherit qt6-cmake
inherit webos_cmake

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};"
S = "${WORKDIR}/git"

do_install:append() {
    if [ "${sysconfdir}" != "${OE_QMAKE_PATH_QT_SETTINGS}" ] ; then
        cp -R --no-dereference --preserve=mode,links -v ${D}${sysconfdir}/xdg ${D}${OE_QMAKE_PATH_QT_SETTINGS}
    fi
}

FILES:${PN} += " \
    ${libdir} \
    xdg/QtProject/Sensors.conf \
"
