# Copyright (c) 2012-2019 LG Electronics, Inc.

require nodejs-module-webos.inc

SUMMARY = "A module for nodejs that allows Javascript access to the webOS system bus"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"

DEPENDS += "glib-2.0 luna-service2"

PV = "3.0.1-1+git${SRCPV}"
SRCREV = "f6c4050a5046c37b1d299a256db10973fdd65cbf"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}.tar.xz;name=node \
"
WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg"

inherit webos_system_bus
inherit pkgconfig

do_configure() {
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    # used by binding.gyp
    export webos_servicesdir="${webos_servicesdir}" webos_prefix="${webos_prefix}"
    node-gyp --arch ${TARGET_ARCH} --nodedir "${WORKDIR}/node-v${NODE_VERSION}" configure
}

do_compile() {
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    # used by binding.gyp
    export webos_servicesdir="${webos_servicesdir}" webos_prefix="${webos_prefix}"
    node-gyp --arch ${TARGET_ARCH} build
}

WEBOS_NODE = "webos-sysbus.node"
do_install:append() {
    install ${S}/src/palmbus.js ${D}${libdir}/nodejs/palmbus.js

    # The CMake build did this with macros
    install -d ${D}${webos_sysbus_rolesdir}
    sed "s|@WEBOS_INSTALL_BINDIR@|$bindir|" < ${S}/files/sysbus/com.webos.nodejs.role.json.in > ${D}${webos_sysbus_rolesdir}/com.webos.nodejs.role.json
}
