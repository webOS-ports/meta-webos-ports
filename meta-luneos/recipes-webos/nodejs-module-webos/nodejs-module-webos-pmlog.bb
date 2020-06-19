# Copyright (c) 2012-2014 LG Electronics, Inc.

require nodejs-module-webos.inc

SUMMARY = "A module for nodejs that allows Javascript access to the Open webOS logging system"

DEPENDS += "pmloglib vim-native"

PV = "3.0.1-1+git${SRCPV}"
SRCREV = "ca359ed90389c5be085e7eac0548236312743d6a"

inherit pkgconfig

do_configure() {
    cd src
    sh -c "xxd -i pmloglib.js > pmloglib.js.h"
    cd ..
    node-gyp --arch ${TARGET_ARCH} --nodedir "${WORKDIR}/node-v${NODE_VERSION}" configure
}

do_compile() {
    node-gyp --arch ${TARGET_ARCH} build
}

WEBOS_NODE = "pmloglib.node"
