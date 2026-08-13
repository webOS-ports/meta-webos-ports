# Copyright (c) 2016-2025 LG Electronics, Inc.

# Maintained by Seungho Park <seunghoh.park@lge.com>
DESCRIPTION = "A tool to convert jsdoc to typescript definition files"
AUTHOR = "EnactUnassigned <enact.swp@lge.com>"
SECTION = "webos/devel/tools"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;beginline=114;md5=06c21a8c43ba289feb62c713df825cc0"

# See enact-dev-native.bb for why this no longer uses "inherit npm" and the
# npmsw:// fetcher: both are disabled in bitbake 2.18 (wrynose).
inherit webos_npm_env
inherit webos_enact_repo
inherit native

DEPENDS = "nodejs-native"

PR = "r1"

SRC_URI = " \
    ${ENACTJS_GIT_REPO}/jsdoc-to-ts.git;name=jsdoc-to-ts${WEBOS_GIT_PROTOCOL};nobranch=1 \
    file://npm-shrinkwrap.json \
"

PV = "1.0.6"
SRCREV = "f20070af2612e8bf434ea6aa38782d6f30118ccc"

NPM_PKG_NAME = "@enact/jsdoc-to-ts"

do_configure[noexec] = "1"

# Unlike enactjs/cli this repo ships only a package-lock.json, so the
# npm-shrinkwrap.json beside this recipe is the sole pin. npm prefers
# npm-shrinkwrap.json over package-lock.json when both are present.
do_compile:prepend() {
    install -m 0644 ${UNPACKDIR}/npm-shrinkwrap.json ${S}/npm-shrinkwrap.json
}

do_compile() {
    cd ${S}
    ${WEBOS_NPM_BIN} ${WEBOS_NPM_INSTALL_FLAGS} install
}

do_compile[network] = "1"

do_install() {
    install -d ${D}${nonarch_libdir}/node_modules/${NPM_PKG_NAME}
    cp -R --no-dereference --preserve=mode,links ${S}/. ${D}${nonarch_libdir}/node_modules/${NPM_PKG_NAME}/

    # webos_enactjs_env.bbclass invokes ${STAGING_BINDIR_NATIVE}/jsdoc-to-ts
    install -d ${D}${bindir}
    ln -rs ${D}${nonarch_libdir}/node_modules/${NPM_PKG_NAME}/bin/jsdoc-to-ts.js ${D}${bindir}/jsdoc-to-ts
    chmod +x ${D}${nonarch_libdir}/node_modules/${NPM_PKG_NAME}/bin/jsdoc-to-ts.js
}

do_install[cleandirs] = "${D}"
