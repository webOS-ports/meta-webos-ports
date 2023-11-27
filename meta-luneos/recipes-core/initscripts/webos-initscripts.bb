# Copyright (c) 2014-2018 LG Electronics, Inc.

SUMMARY = "Event-driven startup scripts for system services"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

RDEPENDS:${PN} = "${VIRTUAL-RUNTIME_init_manager}"
RPROVIDES:${PN} = "initscripts initscripts-functions"
PROVIDES = "initscripts"

# TODO: systemd dependency is for fake initctl.
# The dependency needs to be deleted after deleting fake initctl.
DEPENDS = "systemd"

SRCREV = "ad3eb10e7edb6a7e916eafec8b8671e2073c9e6b"
inherit webos_cmake
inherit webos_ports_ose_repo
inherit systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-CMakeLists.txt-don-t-install-initctl-twice-and-respe.patch \
"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "default-webos.target \
    webos-cbd.target \
    webos-ibd.target \
    webos-rbd.target \
    webos-bd.target \
    webos-dis.target \
    webos-mbd.target \
"

S = "${WORKDIR}/git"

FILES:${PN} += "${base_sbindir}"
