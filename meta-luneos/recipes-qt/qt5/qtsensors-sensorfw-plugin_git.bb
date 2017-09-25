SUMMARY = "Sensorfw plugin for qtsensors"
LICENSE = "LGPL-2.1 | LGPL-3.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE.LGPLv21;md5=cff17b12416c896e10ae2c17a64252e7 \
    file://LICENSE.LGPLv3;md5=c1939be5579666be947371bc8120425f \
    file://LGPL_EXCEPTION.txt;md5=0145c4d1b6f96a661c2c139dfb268fb6 \
"

DEPENDS = "qtbase libhybris virtual/android-headers qtsensors sensorfw"
RDEPENDS_${PN} = "sensorfw bash"

# We're depending on libhybris so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "5.8.0+git${SRCPV}"
SRCREV = "ed4b46adfa7b5699466f12cee2f7ff23998ba870"

inherit webos_ports_repo
inherit qmake5

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE};"
S = "${WORKDIR}/git"

do_install_append() {
    cp -R --no-dereference --preserve=mode,links -v ${D}/etc/xdg ${D}${OE_QMAKE_PATH_QT_SETTINGS}
}

FILES_${PN}-dbg += " \
    ${OE_QMAKE_PATH_PLUGINS}/sensors/.debug \
"

FILES_${PN} += " \
    ${libdir} \
"
