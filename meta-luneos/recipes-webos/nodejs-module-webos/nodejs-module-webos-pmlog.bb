# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "A module for nodejs that allows Javascript access to the Open webOS logging system"
SECTION = "webos/nodejs/module"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "pmloglib node-gyp-native vim-native"

PV = "3.0.1-1+git${SRCPV}"
SRCREV = "6cc2ea137fcb7dbe99c67f2c0e07531d444d03b9"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit pkgconfig
inherit pythonnative

export PYTHON = "python"

NODE_VERSION = "10.15.3"

WEBOS_GIT_PARAM_BRANCH = "tofe/nodejs12"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}.tar.xz;name=node \
"

SRC_URI[node.md5sum] = "d76210a6ae1ea73d10254947684836fb"
SRC_URI[node.sha256sum] = "4e22d926f054150002055474e452ed6cbb85860aa7dc5422213a2002ed9791d5"

S = "${WORKDIR}/git"

do_configure() {
    export HOME=${WORKDIR}
    export LD="${CXX}"
    cd src
    sh -c "xxd -i pmloglib.js > pmloglib.js.h"
    cd ..
    node-gyp --arch ${TARGET_ARCH} --nodedir "${WORKDIR}/node-v${NODE_VERSION}" configure
}

do_compile() {
    export HOME=${WORKDIR}
    export LD="${CXX}"
    node-gyp --arch ${TARGET_ARCH} build
}

do_install() {
    install -d ${D}${libdir}/nodejs
    install ${S}/build/Release/pmloglib.node ${D}${libdir}/nodejs/pmloglib.node
}

# XXX Temporarily add symlink to old location until all users of it are changed
FILES_${PN} += "${webos_prefix}/nodejs"
do_install_append() {
    install -d ${D}${webos_prefix}/nodejs
    ln -svnf ${libdir}/nodejs/pmloglib.node ${D}${webos_prefix}/nodejs/
}

FILES_${PN} += "${libdir}/nodejs"
