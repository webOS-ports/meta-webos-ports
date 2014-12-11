SUMMARY = "node-dbus is a D-Bus binding for Node.js"
SECTION = "nodejs/module"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://README.md;beginline=9;endline=30;md5=baac9b5d8b01797083b581b8cda50f3a"

DEPENDS = "node-gyp-native dbus expat"

PV = "0.2.8+gitr${SRCPV}"

SRC_URI = "git://github.com/Shouqun/node-dbus.git;branch=master;protocol=git"
SRCREV = "e856450b412530968cdeeacf2805b597e04bba28"
S = "${WORKDIR}/git"

do_configure() {
    export LD="${CXX}"
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    node-gyp --arch ${TARGET_ARCH} configure
}

do_compile() {
    export LD="${CXX}"
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    node-gyp --arch ${TARGET_ARCH} build
}

do_install() {
    install -d ${D}${libdir}/nodejs
    install ${S}/build/Release/dbus.node ${D}${libdir}/nodejs/
}

FILES_${PN} += "${libdir}/nodejs"
FILES_${PN}-dbg += "${libdir}/nodejs/.debug"
