SUMMARY = "Asynchronous, non-blocking SQLite3 bindings for Node.js"
SECTION = "nodejs/module"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=79558839a9db3e807e4ae6f8cd100c1c"

DEPENDS = "node-gyp-native sqlite3"

PV = "3.1.13+git${SRCPV}"

inherit webos_npm_env

SRC_URI = " \
    git://github.com/mapbox/node-sqlite3.git;branch=master;protocol=https \
    file://no-node-pre-gyp.patch \
"
SRCREV = "72bddafddcd9285de2e650e9e281b92b9a9e6b85"
S = "${WORKDIR}/git"

do_configure() {
    export LD="${CXX}"
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    ${WEBOS_NODE_GYP} configure
}

do_compile() {
    export LD="${CXX}"
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    ${WEBOS_NODE_GYP} build
}

do_install() {
    install -d ${D}${libdir}/nodejs/sqlite3
    install ${S}/build/Release/node_sqlite3.node ${D}${libdir}/nodejs/
    install ${S}/lib/sqlite3.js ${D}${libdir}/nodejs/sqlite3/
    install ${S}/lib/index.js ${D}${libdir}/nodejs/sqlite3/
    install ${S}/lib/trace.js ${D}${libdir}/nodejs/sqlite3/
}

FILES:${PN} += "${libdir}/nodejs"
