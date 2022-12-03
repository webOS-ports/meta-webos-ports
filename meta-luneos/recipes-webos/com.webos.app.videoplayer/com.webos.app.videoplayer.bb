# Copyright (c) 2021 LG Electronics, Inc.

SUMMARY = "Video Player application"
AUTHOR = "Anish TD <anish.td@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=3072ffcf5bdbbc376ed21c9d378d14d5 \
"

inherit webos_enactjs_app
inherit webos_public_repo

PV = "0.0.1-11+git${SRCPV}"
SRCREV = "a25126d0d36b66639b3abab4462c2ee55c68cea9"

#WEBOS_GIT_PARAM_BRANCH = "herrie/enhanced-acg"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# Workaround for network access issue during do_compile task
do_compile[network] = "1"

WEBOS_ENACTJS_APP_ID = "com.webos.app.videoplayer"
WEBOS_ENACTJS_SHRINKWRAP_OVERRIDE = "false"
WEBOS_LOCALIZATION_DATA_PATH = "${S}"
WEBOS_LOCALIZATION_XLIFF_BASENAME = "videoplayer"
