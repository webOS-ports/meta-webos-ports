SUMMARY = "Memos app re-written from scratch for Open webOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

inherit webos_ports_repo
inherit allarch
inherit webos_enyojs_application
inherit webos_filesystem_paths
inherit webos_app

PV = "1.0.8+git"
SRCREV = "5efeac7d4756e0c93cc6ab39fb831d1168c2ebcd"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
