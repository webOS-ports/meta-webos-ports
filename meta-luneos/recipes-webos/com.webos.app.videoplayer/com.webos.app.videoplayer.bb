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
inherit webos_ports_ose_repo

PV = "0.0.1-11+git${SRCPV}"
SRCREV = "b4079abb02f11a3b0a655a9727ca7acd28737534"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_ENACTJS_APP_ID = "com.webos.app.videoplayer"
WEBOS_ENACTJS_SHRINKWRAP_OVERRIDE = "false"
WEBOS_LOCALIZATION_DATA_PATH = "${S}"
WEBOS_LOCALIZATION_XLIFF_BASENAME = "videoplayer"
