# Copyright (c) 2016-2025 LG Electronics, Inc.

# Maintained by Seungho Park <seunghoh.park@lge.com>
DESCRIPTION = "enact-dev command-line tools used by webOS"
AUTHOR = "EnactUnassigned <enact.swp@lge.com>"
SECTION = "webos/devel/tools"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9456eea7fa7e9e4a4fcdf8e430bd36c8"

# This used to be "inherit npm" with an npmsw:// SRC_URI. bitbake 2.18 (wrynose)
# disables both the npm and npmsw fetchers - they accepted checksums from the
# upstream registry instead of verifying against recipe data, and nobody
# volunteered to fix it, so the OE TSC had them turned off:
#   https://git.openembedded.org/bitbake/commit/?id=355cd226e0720a9ed7683bb01c8c0a58eee03664
# A recipe still using them is silently SkipRecipe'd at parse time, which would
# take out every Enact app, since webos_enactjs_env.bbclass DEPENDS on this.
#
# So do what localization-tool-native already does in this layer: fetch the
# source over git and let npm resolve dependencies at do_compile against the
# curated npm-shrinkwrap.json shipped beside this recipe.
inherit webos_npm_env
inherit webos_enact_repo
inherit native

DEPENDS = "nodejs-native"

# NOTE: It's only necessary to bump PR if the recipe itself changes
# No need to bump PR when changing the values of PV and SRCREV (below)
PR = "r1"

RDEPENDS:${PN} += "jsdoc-to-ts-native"

SRC_URI = " \
    ${ENACTJS_GIT_REPO}/cli.git;name=main${WEBOS_GIT_PROTOCOL};nobranch=1 \
    file://npm-shrinkwrap.json \
"

# PV is the version of the cli distribution, as tagged in the
# enactjs/cli repository on GitHub. This version should correspond to the
# tag whose hash is specified in SRCREV, so PV and SRCREV will always change
# together.

PV = "6.1.3"
SRCREV = "3f4d28c3d4a865090943694515c972631ead699c"

# The package name as npm knows it; the tree has to be installed under this
# name or node's resolver will not find it by require('@enact/cli').
NPM_PKG_NAME = "@enact/cli"

do_configure[noexec] = "1"

# The repo carries its own npm-shrinkwrap.json, but the one beside this recipe
# is the version LuneOS has actually validated, so it wins. npm prefers
# npm-shrinkwrap.json over package-lock.json, and webos_npm_env has already set
# package-lock=true and prefer-offline=true by the time this runs.
do_compile:prepend() {
    install -m 0644 ${UNPACKDIR}/npm-shrinkwrap.json ${S}/npm-shrinkwrap.json
}

do_compile() {
    cd ${S}
    ${WEBOS_NPM_BIN} ${WEBOS_NPM_INSTALL_FLAGS} install
}

# npm resolves dependencies from the registry here, exactly as it did inside the
# old npmsw fetcher - the difference is that it now happens in a task we control.
do_compile[network] = "1"

do_install() {
    install -d ${D}${nonarch_libdir}/node_modules/${NPM_PKG_NAME}
    cp -R --no-dereference --preserve=mode,links ${S}/. ${D}${nonarch_libdir}/node_modules/${NPM_PKG_NAME}/

    # webos_enactjs_env.bbclass invokes ${STAGING_BINDIR_NATIVE}/enact
    install -d ${D}${bindir}
    ln -rs ${D}${nonarch_libdir}/node_modules/${NPM_PKG_NAME}/bin/enact.js ${D}${bindir}/enact
    chmod +x ${D}${nonarch_libdir}/node_modules/${NPM_PKG_NAME}/bin/enact.js
}

# node-gyp leaves object files and static libs behind in dependencies that build
# native addons; npm.bbclass used to strip these and QA still objects to them.
do_install[cleandirs] = "${D}"
