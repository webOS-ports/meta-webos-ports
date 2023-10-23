# Copyright (c) 2012-2023 LG Electronics, Inc.

require nodejs-module-webos.inc

AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SUMMARY = "A module for nodejs that allows Javascript access to the webOS system bus"
LIC_FILES_CHKSUM += "file://oss-pkg-info.yaml;md5=ab2a5dc5745e5204bf0926e2d6ccf877"

DEPENDS += "glib-2.0 luna-service2"

inherit webos_system_bus
inherit pkgconfig

WEBOS_VERSION = "3.0.1-15_a8d17fa1037cd0056449a95aa22c01ded2989d85"
PR = "r17"

PV = "3.0.1-15+git${SRCPV}"
SRCREV = "a8d17fa1037cd0056449a95aa22c01ded2989d85"

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

SRC_URI += " \
    file://0001-Fix-off-by-one-error-preventing-ACG-security-from-fu.patch \
    file://0002-com.webos.nodejs.role.json-Add-permissions-required-.patch \
    file://0003-LS2Handle-findMyAppId-might-not-work-with-mojoservic.patch \
"
