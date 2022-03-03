# Copyright (c) 2019-2021 LG Electronics, Inc.

SUMMARY = "General System Volume UI application"
AUTHOR = "Jongson Kim<jongson.kim@lge.com>"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327 \
    file://oss-pkg-info.yaml;md5=0ec407cd2d4a192e0c60888f4ec66dd7 \
"

inherit webos_enactjs_app
inherit webos_ports_ose_repo

PV = "0.1.0-10+git${SRCPV}"
SRCREV = "0775091614c06087c2a4afcfea509f74bea49a9a"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

# Workaround for network access issue during do_compile task
do_compile[network] = "1"

WEBOS_ENACTJS_SHRINKWRAP_OVERRIDE = "false"

WEBOS_ENACTJS_APP_ID = "com.webos.app.volume"
