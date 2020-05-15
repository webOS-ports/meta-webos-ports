# Copyright (c) 2012-2019 LG Electronics, Inc.

SUMMARY = "A module for nodejs that allows Javascript access to the webOS system bus"
AUTHOR = "Anatolii Sakhnik <anatolii.sakhnik@lge.com>"
SECTION = "webos/nodejs/module"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 luna-service2 node-gyp-native"

PV = "3.0.1-1+git${SRCPV}"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_system_bus
inherit pythonnative

export PYTHON = "python"

NODE_VERSION = "10.15.3"

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}.tar.xz;name=node \
"

SRC_URI[node.md5sum] = "d76210a6ae1ea73d10254947684836fb"
SRC_URI[node.sha256sum] = "4e22d926f054150002055474e452ed6cbb85860aa7dc5422213a2002ed9791d5"

S = "${WORKDIR}/git"

SRCREV = "677c9eef78b0f489288ea0c4dc04489894246037"
do_configure() {
    export HOME=${WORKDIR}
    export LD="${CXX}"
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    # used by binding.gyp
    export webos_servicesdir="${webos_servicesdir}" webos_prefix="${webos_prefix}"
    node-gyp --arch ${TARGET_ARCH} --nodedir "${WORKDIR}/node-v${NODE_VERSION}" configure
}

do_compile() {
    export HOME=${WORKDIR}
    export LD="${CXX}"
    export GYP_DEFINES="sysroot=${STAGING_DIR_HOST}"
    # used by binding.gyp
    export webos_servicesdir="${webos_servicesdir}" webos_prefix="${webos_prefix}"
    node-gyp --arch ${TARGET_ARCH} build
}

do_install() {
    install -d ${D}${libdir}/nodejs
    install ${S}/build/Release/webos-sysbus.node ${D}${libdir}/nodejs/webos-sysbus.node
    install ${S}/src/palmbus.js ${D}${libdir}/nodejs/palmbus.js
}

# XXX Temporarily add symlink to old location until all users of it are changed
FILES_${PN} += "${webos_prefix}/nodejs"
do_install_append() {
    install -d ${D}${webos_prefix}/nodejs
    ln -svnf ${libdir}/nodejs/palmbus.js ${D}${webos_prefix}/nodejs/
    # The CMake build did this with macros
    install -d ${D}${webos_sysbus_rolesdir}
    sed "s|@WEBOS_INSTALL_BINDIR@|$bindir|" < ${S}/files/sysbus/com.webos.nodejs.role.json.in > ${D}${webos_sysbus_rolesdir}/com.webos.nodejs.role.json
}

FILES_${PN} += "${libdir}/nodejs"
