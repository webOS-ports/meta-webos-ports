# Copyright (c) 2019-2021 LG Electronics, Inc.

SUMMARY = "General System Launcher application"
AUTHOR = "Kiho Choi<kiho.choi@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=0ec407cd2d4a192e0c60888f4ec66dd7 \
"

inherit webos_enactjs_app
inherit webos_public_repo

PV = "0.1.0-10+git${SRCPV}"
SRCREV = "373d57006e53af2b59c15888b846230e2420f30b"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

# Workaround for network access issue during do_compile task
do_compile[network] = "1"

do_install:prepend() {
    export NODE_OPTIONS="--openssl-legacy-provider"
    # work around nodejs trying to load openssl's legacy.so from openssl WORKDIR which might be already removed by rm-work
    # see https://lists.openembedded.org/g/openembedded-devel/message/96799
    export OPENSSL_MODULES="${STAGING_LIBDIR_NATIVE}/ossl-modules/"
}

WEBOS_ENACTJS_SHRINKWRAP_OVERRIDE = "false"

WEBOS_ENACTJS_APP_ID = "com.webos.app.notification"
