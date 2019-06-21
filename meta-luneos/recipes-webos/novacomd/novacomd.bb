# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "NovaCOMd -- Daemon for NovaCOM (device and host)"
AUTHOR = "Steve Lemke <steve.lemke@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "2.0.0-124+git${SRCPV}"
SRCREV = "d46e96480129a2ce318914878c5e097888abf8ff"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_machine_impl_dep

DEPENDS  = "nyx-lib"
RDEPENDS_${PN} = "${@oe.utils.conditional('WEBOS_TARGET_MACHINE_IMPL','emulator','iproute2','',d)}"

WEBOS_GIT_PARAM_BRANCH = "webOS-ports/webOS-OSE"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://novacomd.conf \
"

S = "${WORKDIR}/git"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

do_install_append() {
    install -d ${D}${sysconfdir}/init
    install -m 0644 ${WORKDIR}/novacomd.conf ${D}${sysconfdir}/init/novacomd.conf
}
