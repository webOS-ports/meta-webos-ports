# Copyright (c) 2013-2023 LG Electronics, Inc.

SUMMARY = "A userspace service that provides access to the webOS database"
SECTION = "webos/base"
AUTHOR = "Yogish S <yogish.s@lge.com>"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

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
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

WEBOS_VERSION = "3.2.0-29_bc18024e9cb039d7eba18c9266b54128f25a3dbd"
PR = "r40"

EXTRA_OECMAKE += "-DWEBOS_DB8_BACKEND:STRING='leveldb;sandwich' -DCMAKE_SKIP_RPATH:BOOL=TRUE"
EXTRA_OECMAKE:append:class-target = " -DWEBOS_CONFIG_BUILD_TESTS:BOOL=TRUE  -DUSE_PMLOG:BOOL=TRUE  -DBUILD_LS2:BOOL=TRUE -DWANT_PROFILING:BOOL=${@ 'true' if '${WEBOS_DISTRO_PRERELEASE}' != '' else 'false'}"
EXTRA_OECMAKE:append:class-native = " -DWEBOS_CONFIG_BUILD_TESTS:BOOL=FALSE -DUSE_PMLOG:BOOL=FALSE -DBUILD_LS2:BOOL=FALSE"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-com.palm.db.role.json.in-More-generic-app-access.patch \
    file://0002-db-Update-db8.groups.json-for-test-API-s.patch \
"

S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "db8-maindb.service db8-mediadb.service db8-pre-config.service db8-tempdb.service db8.service"
WEBOS_SYSTEMD_SCRIPT = "db8-maindb.sh"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm -f ${D}${sysconfdir}/systemd/system/db8-maindb.service
    rm -f ${D}${sysconfdir}/systemd/system/scripts/db8-maindb.sh
    rm -f ${D}${sysconfdir}/systemd/system/db8-mediadb.service
    rm -f ${D}${sysconfdir}/systemd/system/db8-pre-config.service
    rm -f ${D}${sysconfdir}/systemd/system/db8-tempdb.service
    rm -f ${D}${sysconfdir}/systemd/system/db8.service
}

PACKAGES =+ "${PN}-tests"

FILES:${PN}-tests = "${libdir}/${PN}/tests"
FILES:${PN} += "${webos_sysbus_datadir}"

BBCLASSEXTEND = "native"
