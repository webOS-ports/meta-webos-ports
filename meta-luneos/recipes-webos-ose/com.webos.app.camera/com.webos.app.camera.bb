# Copyright (c) 2021-2025 LG Electronics, Inc.

SUMMARY = "Camera application"
AUTHOR = "VINH VAN LE <vinh5.le@lge.com>"
SECTION = "webos/apps"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=3072ffcf5bdbbc376ed21c9d378d14d5 \
"

WEBOS_VERSION = "0.0.1-18_62dac33d771e4a3b14bf740dccf3323793211231"
PR = "r5"

inherit webos_component
inherit webos_enhanced_submissions
inherit webos_enactjs_app
inherit webos_public_repo

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

WEBOS_ENACTJS_APP_ID = "com.webos.app.camera"

# FIXME: Workaround for network access issue during do_npm_install task
do_npm_install[network] = "1"

# WRR-13760 host-user-contaminated with new nodejs-22.12.0
do_install:append() {
    chown -R root:root ${D}/
}
