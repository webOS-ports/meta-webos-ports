SUMMARY = "Preware is a webOS on-device homebrew installer."
SECTION = "webos/apps"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_ose_repo
inherit webos_filesystem_paths
inherit allarch
inherit webos_enyojs_application
inherit webos_app

PV = "2.1.3+git"
SRCREV = "650abe3bc61a17fe730a2d7f2c2854ddf624a7dd"

# Recipe name is the app id, because webos_application derives
# WEBOS_APPLICATION_NAME from PN and installs into
# ${webos_applicationsdir}/${WEBOS_APPLICATION_NAME}. Preware 2 declares
# "com.palm.app.preware2" in its appinfo.json, and webOS requires the directory
# to match the id - installed as org.webosports.app.preware the app did not
# register at all. The repository is still called preware.
WEBOS_REPO_NAME = "preware"

# Supersede the pre-rename package so an upgrade removes it instead of leaving
# both ids installed.
RREPLACES:${PN} = "org.webosports.app.preware"
RCONFLICTS:${PN} = "org.webosports.app.preware"
RPROVIDES:${PN} = "org.webosports.app.preware"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
