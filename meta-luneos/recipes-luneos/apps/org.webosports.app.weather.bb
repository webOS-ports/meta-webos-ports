SUMMARY = "Weather: forecasts from Open-Meteo, with a dashboard"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_app

PV = "2.0.0+git"
SRCREV = "6ecaedf2b21e324059df60bbab688574d06484e2"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
