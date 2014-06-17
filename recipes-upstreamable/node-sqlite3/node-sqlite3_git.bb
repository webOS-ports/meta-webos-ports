SUMMARY = "Asynchronous, non-blocking SQLite3 bindings for Node.js"
SECTION = "nodejs/module"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=79558839a9db3e807e4ae6f8cd100c1c"

DEPENDS = "node-gyp-native sqlite3"

PV = "2.2.0+gitr${SRCPV}"

SRC_URI = " \
    git://github.com/mapbox/node-sqlite3.git;branch=master;protocol=git \
    file://no-node-pre-gyp.patch \
"
SRCREV = "c4dff8b95c599508381b9c09678789cb219ea051"
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
    install -d ${D}${libdir}/nodejs/sqlite3
    install ${S}/build/Release/node_sqlite3.node ${D}${libdir}/nodejs/
    install ${S}/lib/sqlite3.js ${D}${libdir}/nodejs/sqlite3/
}

FILES_${PN} += "${libdir}/nodejs"
FILES_${PN}-dbg += "${libdir}/nodejs/.debug"
