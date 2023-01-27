# Copyright (c) 2012-2014 LG Electronics, Inc.

SECTION = "webos/nodejs/module"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "node-gyp-native"

inherit webos_ports_ose_repo
inherit webos_filesystem_paths
inherit webos_npm_env
inherit python3native

export PYTHON = "python3"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    ${WEBOS_NODE_SRC_URI} \
"

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
