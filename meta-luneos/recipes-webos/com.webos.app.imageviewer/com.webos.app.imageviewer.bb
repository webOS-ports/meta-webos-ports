# Copyright (c) 2021 LG Electronics, Inc.

SUMMARY = "Image Viewer application"
AUTHOR = "Anish TD <anish.td@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=3072ffcf5bdbbc376ed21c9d378d14d5 \
"

PV = "0.0.1-13+git${SRCPV}"
SRCREV = "8b5e5845d4a8012c159f193ee359315999244c59"

inherit webos_enactjs_app
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# Workaround for network access issue during do_compile task
do_compile[network] = "1"

do_install:prepend() {
    export NODE_OPTIONS="--openssl-legacy-provider"
    # work around nodejs trying to load openssl's legacy.so from openssl WORKDIR which might be already removed by rm-work
    # see https://lists.openembedded.org/g/openembedded-devel/message/96799
    export OPENSSL_MODULES="${STAGING_LIBDIR_NATIVE}/ossl-modules/"
}

WEBOS_ENACTJS_APP_ID = "com.webos.app.imageviewer"
WEBOS_ENACTJS_SHRINKWRAP_OVERRIDE = "false"
WEBOS_LOCALIZATION_DATA_PATH = "${S}"
WEBOS_LOCALIZATION_XLIFF_BASENAME = "imageviewer"
