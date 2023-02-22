# Copyright (c) 2012-2019 LG Electronics, Inc.

require nodejs-module-webos.inc

SUMMARY = "A module for nodejs that allows Javascript access to the webOS system bus"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"

DEPENDS += "glib-2.0 luna-service2"

PV = "3.0.1-1+git${SRCPV}"
SRCREV = "0a9fd959f4930e443f29a18f8bbeb1658500a131"

inherit webos_system_bus
inherit pkgconfig

do_configure() {
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    # used by binding.gyp
    export webos_servicesdir="${webos_servicesdir}" webos_prefix="${webos_prefix}"
    ${WEBOS_NODE_GYP} configure
}

do_compile() {
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    # used by binding.gyp
    export webos_servicesdir="${webos_servicesdir}" webos_prefix="${webos_prefix}"
    ${WEBOS_NODE_GYP} build
}

WEBOS_NODE = "webos-sysbus.node"
do_install:append() {
    install ${S}/src/palmbus.js ${D}${libdir}/nodejs/palmbus.js

    # The CMake build did this with macros
    install -d ${D}${webos_sysbus_rolesdir}
    sed "s|@WEBOS_INSTALL_BINDIR@|$bindir|" < ${S}/files/sysbus/com.webos.nodejs.role.json.in > ${D}${webos_sysbus_rolesdir}/com.webos.nodejs.role.json
}
