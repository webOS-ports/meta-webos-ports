# Copyright (c) 2021-2023 LG Electronics, Inc.
#
# webos_npm_env
#
# Base-level bbclass to setup the development environment for NPM.
#

def get_nodejs_arch(d):
    target_arch = d.getVar('TRANSLATED_TARGET_ARCH')

    if target_arch == "x86-64":
        target_arch = "x64"
    elif target_arch == "aarch64":
        target_arch = "arm64"
    elif target_arch == "powerpc":
        target_arch = "ppc"
    elif target_arch == "powerpc64":
        target_arch = "ppc64"
    elif (target_arch == "i486" or target_arch == "i586" or target_arch == "i686"):
        target_arch = "ia32"

    return target_arch

WEBOS_NPM_BIN ?= "${STAGING_BINDIR_NATIVE}/npm"
WEBOS_NPM_CACHE_DIR ?= "${WORKDIR}/npm_cache"
WEBOS_NPM_REGISTRY ?= "https://registry.npmjs.org/"
WEBOS_NPM_ARCH ?= "${@get_nodejs_arch(d)}"
WEBOS_NPM_INSTALL_FLAGS ?= "--arch=${WEBOS_NPM_ARCH} --target_arch=${WEBOS_NPM_ARCH} --production --without-ssl --insecure --no-optional --verbose"

WEBOS_NODE_BIN ??= "${STAGING_BINDIR_NATIVE}/node"

# for node-gyp
#
# This MUST track the nodejs version that meta-oe builds for the image
# (meta-openembedded/meta-oe/recipes-devtools/nodejs). node-gyp compiles the
# native modules against these headers, and a native module built against one
# major version of node cannot be loaded by another: the V8 ABI differs.
#
# wrynose moved meta-oe to nodejs 22 while this was still pinned to 20, so every
# module under /usr/lib/nodejs was built against Node 20's V8 headers and then
# failed to load in Node 22 with
#
#   /usr/lib/nodejs/webos.node: undefined symbol:
#   _ZN2v812api_internal18GlobalizeReferenceEPNS_8internal7IsolateEPm
#
# That kills every JS service (run-js-service exits immediately), which is not
# obvious from the outside: com.palm.service.accounts never registers, so no
# local profile is created, /var/luna/preferences/first-use-profile-created is
# never written, BootManager stays in BOOT_STATE_FIRSTUSE and the device
# relaunches First Use on every boot with no gesture bar.
WEBOS_NODE_VERSION = "22.23.1"
WEBOS_NODE_SRC_URI = "https://nodejs.org/dist/v${WEBOS_NODE_VERSION}/node-v${WEBOS_NODE_VERSION}.tar.xz;name=node"
WEBOS_NODE_GYP = "node-gyp --arch '${TARGET_ARCH}' --nodedir '${UNPACKDIR}/node-v${WEBOS_NODE_VERSION}'"
SRC_URI[node.sha256sum] = "b27385d6845089bdb91285d94b06c2a5cf1c37f8173a3c4e10824cc1ffadeaba"

do_compile:prepend() {
    # this is needed to use user's gitconfig even after changing the HOME directory bellow
    # need to check ${HOME}/.gitconfig existence not only because it might be missing in real HOME of given user
    # but also HOME might be already changed to WORKDIR or some other directory somewhere else
    [ "${HOME}" != "${WORKDIR}" -a -e ${HOME}/.gitconfig ] && cp ${HOME}/.gitconfig ${WORKDIR}

    # changing the home directory to the working directory, the .npmrc will be created in this directory
    bbnote "webos_npm_env: set HOME to WORKDIR"
    export HOME=${WORKDIR}

    export NPM_ENV=production

    # configure cache to be in the WORKDIR directory
    bbnote "webos_npm_env: set npm cache"
    ${WEBOS_NPM_BIN} set cache ${WEBOS_NPM_CACHE_DIR}

    # clear local cache prior to each compile
    bbnote "webos_npm_env: clear npm cache and ${S}/node_modules"
    ${WEBOS_NPM_BIN} cache clear --force

    # Prefer using offline cached packages
    bbnote "webos_npm_env: config npm offline"
    ${WEBOS_NPM_BIN} config set prefer-offline true

    # Fix to prevent NPM from not honoring shrinkwrap; see https://github.com/npm/npm/issues/17960
    bbnote "webos_npm_env: config npm package-lock"
    ${WEBOS_NPM_BIN} config set package-lock true

    # configure http proxy if neccessary
    if [ -n "${http_proxy}" ]; then
        bbnote "webos_npm_env: config proxy ${http_proxy}"
        ${WEBOS_NPM_BIN} config set proxy ${http_proxy}
    fi
    if [ -n "${HTTP_PROXY}" ]; then
        bbnote "webos_npm_env: config proxy ${HTTP_PROXY}"
        ${WEBOS_NPM_BIN} config set proxy ${HTTP_PROXY}
    fi

    # explicity set NPM registry URI
    bbnote "webos_npm_env: set npm registry to ${WEBOS_NPM_REGISTRY}"
    ${WEBOS_NPM_BIN} set registry ${WEBOS_NPM_REGISTRY}
}
