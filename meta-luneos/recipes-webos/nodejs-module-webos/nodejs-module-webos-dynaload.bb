# Copyright (c) 2012-2018 LG Electronics, Inc.

require nodejs-module-webos.inc

SUMMARY = "A module for nodejs that allows dynamic loading and execution of Javascript files"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"

DEPENDS += "boost"

PV = "3.0.2-1+git${SRCPV}"
SRCREV = "ab87a7f2c59e96ec91360f3d4a1bc82f42594d6a"

inherit pkgconfig

do_configure() {
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    ${WEBOS_NODE_GYP} configure
}

do_compile() {
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    ${WEBOS_NODE_GYP} build
}

WEBOS_NODE = "webos.node"

SRC_URI += "file://0001-Fix-build-with-nodejs-18.patch"
