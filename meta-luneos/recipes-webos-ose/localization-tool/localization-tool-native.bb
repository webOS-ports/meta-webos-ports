# Copyright (c) 2013-2024 LG Electronics, Inc.
#
# Kept when the rest of recipes-webos-ose/localization-tool was removed for
# wrynose. The ilib-loctool-webos-*-native and loctool-native recipes there all
# used the npm:// fetcher, which bitbake 2.18 disables, and nothing referenced
# them. This one is different: it fetches over git and runs npm at do_compile,
# and webos_localizable.bbclass DEPENDS on it, so every localizable component
# needs it.

SUMMARY = "A localization tool is written in JavaScript"
AUTHOR = "Seonmi Jin <seonmi1.jin@lge.com>"
SECTION = "webos/devel/tools"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

PR = "r10"

inherit webos_npm_env
inherit native
DEPENDS = "nodejs-native node-gyp-packages-native"

SRC_URI = "git://github.com/iLib-js/ilib-loctool-webos-dist.git;branch=main;protocol=https"

# PV is the version of the ilib-loctool-webos-dist distribution, as tagged in the
# iLib-js/ilib-loctool-webos-dist repository on GitHub. This version should correspond to the
# tag whose hash is specified in SRCREV, so PV and SRCREV will always change
# together.
PV = "1.16.0"
SRCREV = "ee0c270a19cf39268ea2314dee6e5bf8bbf44cf4"

# Skip the unwanted tasks
do_configure[noexec] = "1"

do_compile() {
    ${WEBOS_NPM_BIN} ${WEBOS_NPM_INSTALL_FLAGS} install
}

# Install js-loctool in sysroot for use in localization recipes
do_install() {
    install -d ${D}${base_prefix}/opt/js-loctool
    cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}${base_prefix}/opt/js-loctool
}

SYSROOT_DIRS += "${base_prefix}/opt"

# Workaround for network access issue during do_compile task
do_compile[network] = "1"
