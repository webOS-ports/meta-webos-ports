SUMMARY = "Preware is a webOS on-device homebrew installer."
SECTION = "webos/apps"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

inherit webos_ports_ose_repo
inherit webos_filesystem_paths
inherit allarch
inherit webos_enyojs_application
inherit webos_app

PV = "2.0.3+git"
SRCREV = "3337f2ebf0a470c8c54df69083b371db7cfd7943"

WEBOS_REPO_NAME = "preware"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
