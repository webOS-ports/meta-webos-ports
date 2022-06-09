# Copyright (c) 2012-2014 LG Electronics, Inc.

SECTION = "webos/nodejs/module"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "node-gyp-native"

inherit webos_ports_ose_repo
inherit webos_filesystem_paths
inherit python3native

export PYTHON = "python3"

# this needs to be kept in sync with nodejs version in meta-oe
# otherwise we'll get node.js error: like during dunfell upgrade
# where meta-oe had 12.14.1 and 10.15.3 was here:
# The module '/usr/lib/nodejs/webos.node' was compiled against
# a different Node.js version using NODE_MODULE_VERSION 64.
# This version of Node.js requires NODE_MODULE_VERSION 72.
# Please try re-compiling or re-installing
NODE_VERSION = "16.14.2"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}.tar.xz;name=node \
"

SRC_URI[node.sha256sum] = "e922e215cc68eb5f94d33e8a0b61e2c863b7731cc8600ab955d3822da90ff8d1"

S = "${WORKDIR}/git"

do_configure:prepend() {
    export HOME=${WORKDIR}
    export LD="${CXX}"
}

do_compile:prepend() {
    export HOME=${WORKDIR}
    export LD="${CXX}"
}

do_install() {
    install -d ${D}${libdir}/nodejs
    install ${S}/build/Release/${WEBOS_NODE} ${D}${libdir}/nodejs/${WEBOS_NODE}
}

FILES:${PN} += "${libdir}/nodejs"
