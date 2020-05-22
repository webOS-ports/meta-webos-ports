# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "A userspace service that provides access to the webOS database"
SECTION = "webos/base"
AUTHOR = "Maksym Sditanov <maxim.sditanov@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "icu glib-2.0 leveldb leveldb-tl boost"
DEPENDS_append_class-target = " luna-service2 pmloglib jemalloc gtest curl"

# db8 is also the provider for mojodb
PROVIDES = "mojodb"

# db8's upstart job requires stat
VIRTUAL-RUNTIME_stat ?= "stat"
VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS_${PN}_append_class-target = " ${VIRTUAL-RUNTIME_stat} ${VIRTUAL-RUNTIME_bash}"
RDEPENDS_${PN}-tests_append_class-target = " ${VIRTUAL-RUNTIME_bash}"

inherit webos_ports_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_configure_manifest
inherit systemd

EXTRA_OECMAKE += "-DWEBOS_DB8_BACKEND:STRING='leveldb;sandwich' -DCMAKE_SKIP_RPATH:BOOL=TRUE"
EXTRA_OECMAKE_append_class-target = " -DWEBOS_CONFIG_BUILD_TESTS:BOOL=TRUE  -DUSE_PMLOG:BOOL=TRUE  -DBUILD_LS2:BOOL=TRUE -DWANT_PROFILING:BOOL=${@ 'true' if '${WEBOS_DISTRO_PRERELEASE}' != '' else 'false'}"
EXTRA_OECMAKE_append_class-native = " -DWEBOS_CONFIG_BUILD_TESTS:BOOL=FALSE -DUSE_PMLOG:BOOL=FALSE -DBUILD_LS2:BOOL=FALSE"

WEBOS_GIT_PARAM_BRANCH = "herrie/acg"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " \
    ${PN}.service \
    ${PN}-maindb.service \
    ${PN}-mediadb.service \
    ${PN}-pre-config.service \
    ${PN}-tempdb.service \
"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system

    install -m 0644 ${S}/files/systemd/${PN}.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/${PN}-maindb.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/${PN}-mediadb.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/${PN}-pre-config.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/files/systemd/${PN}-tempdb.service ${D}${systemd_unitdir}/system/
}

PV = "3.2.0-5+git${SRCPV}"
SRCREV = "aa6d93618b575ccebf3ab9336cd90e66feabbab9"

PACKAGES =+ "${PN}-tests"

FILES_${PN}-tests = "${libdir}/${PN}/tests"

BBCLASSEXTEND = "native"
