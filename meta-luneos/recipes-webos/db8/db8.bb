# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "A userspace service that provides access to the webOS database"
SECTION = "webos/base"
AUTHOR = "Maksym Sditanov <maxim.sditanov@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "icu glib-2.0 leveldb leveldb-tl boost"
DEPENDS:append:class-target = " luna-service2 pmloglib jemalloc gtest curl"

# db8 is also the provider for mojodb
PROVIDES = "mojodb"

# db8's upstart job requires stat
VIRTUAL-RUNTIME_stat ?= "stat"
VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS:${PN}:append:class-target = " ${VIRTUAL-RUNTIME_stat} ${VIRTUAL-RUNTIME_bash}"
RDEPENDS:${PN}-tests:append:class-target = " ${VIRTUAL-RUNTIME_bash}"

inherit webos_public_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_configure_manifest
inherit systemd
inherit pkgconfig

EXTRA_OECMAKE += "-DWEBOS_DB8_BACKEND:STRING='leveldb;sandwich' -DCMAKE_SKIP_RPATH:BOOL=TRUE"
EXTRA_OECMAKE:append:class-target = " -DWEBOS_CONFIG_BUILD_TESTS:BOOL=TRUE  -DUSE_PMLOG:BOOL=TRUE  -DBUILD_LS2:BOOL=TRUE -DWANT_PROFILING:BOOL=${@ 'true' if '${WEBOS_DISTRO_PRERELEASE}' != '' else 'false'}"
EXTRA_OECMAKE:append:class-native = " -DWEBOS_CONFIG_BUILD_TESTS:BOOL=FALSE -DUSE_PMLOG:BOOL=FALSE -DBUILD_LS2:BOOL=FALSE"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
file://0001-com.palm.db.role.json.in-More-generic-app-access.patch \
"

S = "${WORKDIR}/git"

PV = "3.2.0-22+git${SRCPV}"
SRCREV = "a9697f6aa3b2e26fddb03639ac885574cd608f87"

PACKAGES =+ "${PN}-tests"

FILES:${PN}-tests = "${libdir}/${PN}/tests"
FILES:${PN} += "${webos_sysbus_datadir}"

BBCLASSEXTEND = "native"
