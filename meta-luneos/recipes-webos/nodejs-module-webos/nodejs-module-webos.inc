# Copyright (c) 2012-2014 LG Electronics, Inc.

SECTION = "webos/nodejs/module"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "node-gyp-native"

inherit webos_ports_fork_repo
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
NODE_VERSION = "10.15.3"

WEBOS_GIT_PARAM_BRANCH = "tofe/nodejs12"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}.tar.xz;name=node \
"

SRC_URI[node.md5sum] = "d76210a6ae1ea73d10254947684836fb"
SRC_URI[node.sha256sum] = "4e22d926f054150002055474e452ed6cbb85860aa7dc5422213a2002ed9791d5"

S = "${WORKDIR}/git"

do_configure_prepend() {
    export HOME=${WORKDIR}
    export LD="${CXX}"
}

do_compile_prepend() {
    export HOME=${WORKDIR}
    export LD="${CXX}"
}

do_install() {
    install -d ${D}${libdir}/nodejs
    install ${S}/build/Release/${WEBOS_NODE} ${D}${libdir}/nodejs/${WEBOS_NODE}

    # XXX Temporarily add symlink to old location until all users of it are changed
    install -d ${D}${webos_prefix}/nodejs
    ln -svnf ${libdir}/nodejs/${WEBOS_NODE} ${D}${webos_prefix}/nodejs/
}

# XXX Temporarily add symlink to old location until all users of it are changed
FILES_${PN} += "${webos_prefix}/nodejs"

FILES_${PN} += "${libdir}/nodejs"
