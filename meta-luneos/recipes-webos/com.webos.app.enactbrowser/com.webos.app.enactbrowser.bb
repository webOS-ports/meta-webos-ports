# Copyright (c) 2018-2021 LG Electronics, Inc.

SUMMARY = "Enact Based Web Browser"
AUTHOR = "Mikyung Kim <mikyung27.kim@lge.com>"
SECTION = "webos/apps"
LICENSE = "LicenseRef-EnactBrowser-Evaluation"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=6e00eb832d81f89a0f47fac10db717c7 \
    file://oss-pkg-info.yaml;md5=72b3e3cef46e5ab3e175e5b515dc3b18 \
"

inherit webos_ports_ose_repo
inherit webos_enactjs_app
inherit webos_filesystem_paths
inherit webos_npm_env

PV = "1.0.0-48+git${SRCPV}"
SRCREV = "2e1614a00c913d5d6c9f6689e97fc7ad49a8441a"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"
WEBOS_SYSTEM_BUS_MANIFEST_TYPE = "PASS"

WEBOS_ENACTJS_SHRINKWRAP_OVERRIDE = "false"
WEBOS_ENACTJS_PACK_OPTS = "--isomorphic --production --snapshot"
WEBOS_ENACTJS_ILIB_OVERRIDE = ""
WEBOS_PREFERRED_GFX_IMAGE_FORMAT_ENABLED="0"

WEBOS_ENACTJS_PROJECT_PATH = "./samples/enact-based"
WEBOS_ENACTJS_PACK_OVERRIDE = "\
    ${ENACT_DEV_LEGACY} pack ${WEBOS_ENACTJS_PACK_OPTS} && \
    ${WEBOS_NODE_BIN} resbundler.js dist && \
    rm -fr ./dist/resources && \
    rm -fr ./dist/node_modules/@enact/moonstone/resources && \
    cp -f webos-locale.js dist/webos-locale.js && \
    ln -sfn /usr/share/javascript/ilib/localedata/ ./dist/ilibdata && \
    cp -f label.js dist/ && \
    cp -f background.js dist/ && \
    cp -f defaults.js dist/ && \
    cp -f manifest.json dist/ && \
    ${WEBOS_NODE_BIN} extract-inline.js ./dist \
"

# Remove --production, because that causes
# http://gecko.lge.com/Errors/Details/119724
# Error: Cannot find module 'glob'
WEBOS_NPM_INSTALL_FLAGS = "--arch=${WEBOS_NPM_ARCH} --target_arch=${WEBOS_NPM_ARCH} --without-ssl --insecure --no-optional --verbose"

# Workaround for network access issue during do_compile task
do_compile[network] = "1"

do_install:prepend() {
    export NODE_OPTIONS="--openssl-legacy-provider"
    # work around nodejs trying to load openssl's legacy.so from openssl WORKDIR which might be already removed by rm-work
    # see https://lists.openembedded.org/g/openembedded-devel/message/96799
    export OPENSSL_MODULES="${STAGING_LIBDIR_NATIVE}/ossl-modules/"
}

do_compile:append() {
    ${WEBOS_NPM_BIN} ${WEBOS_NPM_INSTALL_FLAGS} install
    ${WEBOS_NODE_BIN} ./scripts/cli.js transpile
}

install_acg_configuration() {
    # sysbus files *.json
    install -d ${D}${webos_sysbus_permissionsdir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -d ${D}${webos_sysbus_manifestsdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.perm.json ${D}${webos_sysbus_permissionsdir}/${BPN}.app.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.role.json ${D}${webos_sysbus_rolesdir}/${BPN}.app.json
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${BPN}.manifest.json ${D}${webos_sysbus_manifestsdir}/${BPN}.json
}

do_install:append() {
    install_acg_configuration

    # Enact does something wrong in this case, chown to prevent host-user-contaminated QA issue
    # but should be fixed in enactjs
    chown root:root -R ${D}
}

FILES:${PN} += "${webos_applicationsdir}"
