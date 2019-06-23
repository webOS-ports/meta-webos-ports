# Copyright (c) 2014-2018 LG Electronics, Inc.

SUMMARY = "Event-driven startup scripts for system services"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

VIRTUAL-RUNTIME_rdx-utils ?= "rdxd"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_init_manager} ${VIRTUAL-RUNTIME_rdx-utils}"
RPROVIDES_${PN} = "initscripts initscripts-functions"
PROVIDES = "initscripts"

# TODO: systemd dependency is for fake initctl.
# The dependency needs to be deleted after deleting fake initctl.
DEPENDS = "systemd"

SRCREV = "e93c264aa385a4a11b6d4df547662581eb4207a8"
inherit webos_cmake
inherit webos_ports_fork_repo
inherit systemd

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "default-webos.target \
    webos-cbd.target \
    webos-ibd.target \
    webos-rbd.target \
    webos-bd.target \
    webos-dis.target \
    webos-mbd.target \
"

S = "${WORKDIR}/git"
