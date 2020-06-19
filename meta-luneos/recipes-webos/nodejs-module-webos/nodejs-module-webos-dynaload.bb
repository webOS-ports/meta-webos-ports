# Copyright (c) 2012-2018 LG Electronics, Inc.

require nodejs-module-webos.inc

SUMMARY = "A module for nodejs that allows dynamic loading and execution of Javascript files"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"

DEPENDS += "boost"

PV = "3.0.2-1+git${SRCPV}"
SRCREV = "b1ec2c559d5a53280b604230d9481113c09ba069"

inherit pkgconfig

do_configure() {
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    node-gyp --arch ${TARGET_ARCH} --nodedir "${WORKDIR}/node-v${NODE_VERSION}" configure
}

do_compile() {
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    node-gyp --arch ${TARGET_ARCH} build
}

WEBOS_NODE = "webos.node"
