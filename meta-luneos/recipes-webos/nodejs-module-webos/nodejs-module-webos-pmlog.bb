# Copyright (c) 2012-2014 LG Electronics, Inc.

require nodejs-module-webos.inc

SUMMARY = "A module for nodejs that allows Javascript access to the Open webOS logging system"

DEPENDS += "pmloglib vim-native"

PV = "3.0.1-1+git${SRCPV}"
SRCREV = "a2f85331a39bfbf98936e548e5676ff7aa3d0114"

inherit pkgconfig

do_configure() {
    cd src
    sh -c "xxd -i pmloglib.js > pmloglib.js.h"
    cd ..
    ${WEBOS_NODE_GYP} configure
}

do_compile() {
    ${WEBOS_NODE_GYP} build
}

WEBOS_NODE = "pmloglib.node"

SRC_URI += "file://0001-Fix-build-with-nodejs-18.patch"
