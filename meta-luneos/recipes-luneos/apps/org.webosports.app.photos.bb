SUMMARY = "Photos & Videos app re-written from scratch in Enyo 2"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_application

PV = "0.0.1+git"
SRCREV = "58edaee3195cb4f974a2e914f39c86cde8d48f83"
# 58edaee3 is on herrie/photos-qml (org.webosports.app.photos PR #5), not yet merged into master; back to that branch once it is.
WEBOS_GIT_PARAM_BRANCH = "herrie/photos-qml"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
