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
inherit webos_ports_ose_repo

PV = "0.1.0-10+git${SRCPV}"
SRCREV = "7096417c6f83ca4b9016defff9eabc8ff2564c39"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

WEBOS_ENACTJS_SHRINKWRAP_OVERRIDE = "false"

WEBOS_ENACTJS_APP_ID = "com.webos.app.notification"
