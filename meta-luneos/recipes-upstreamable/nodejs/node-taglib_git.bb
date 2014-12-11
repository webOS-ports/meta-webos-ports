SUMMARY = "node-taglib is a simple binding to TagLib in Javascript"
SECTION = "nodejs/module"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=496b4c45fbf47bb331ecf4d6863327a3"

DEPENDS = "node-gyp-native taglib"

PV = "0.8.0+gitr${SRCPV}"

SRC_URI = "git://github.com/nikhilm/node-taglib.git \
    file://0001-binding.gyp-Use-pkg-config-to-find-taglib-flags.patch \
"
SRCREV = "a61fbbd4c8a759e639f766132dc34060a98f3c23"
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
    install ${S}/build/Release/taglib.node ${D}${libdir}/nodejs/
}

FILES_${PN} += "${libdir}/nodejs"
FILES_${PN}-dbg += "${libdir}/nodejs/.debug"
