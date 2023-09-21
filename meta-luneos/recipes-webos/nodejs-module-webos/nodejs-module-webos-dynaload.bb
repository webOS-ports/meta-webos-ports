# Copyright (c) 2012-2023 LG Electronics, Inc.

require nodejs-module-webos.inc

AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SUMMARY = "A module for nodejs that allows dynamic loading and execution of Javascript files"
#FIXME: Use below after bumping
#LIC_FILES_CHKSUM += "file://oss-pkg-info.yaml;md5=d980a35955bef62f4f06ee11e344c4d0"

DEPENDS += "boost"

WEBOS_VERSION = "3.0.2-4_ef029ca96241caf25700b51a47502e6752cc8638"
PR = "r13"

PV = "3.0.2-4+git${SRCPV}"
SRCREV = "ef029ca96241caf25700b51a47502e6752cc8638"

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
