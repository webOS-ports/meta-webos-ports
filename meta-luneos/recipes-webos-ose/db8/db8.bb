# Copyright (c) 2013-2025 LG Electronics, Inc.

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

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: what used to be the
# 0001-0003 (plus the two role/groups patches applied separately below) patch
# series in this directory now lives as commits on that branch, so nothing is
# applied on top any more. Pinned with a plain SRCREV - submission tags are a
# webosose convention and this branch carries none. The branch itself is set
# below via WEBOS_GIT_PARAM_BRANCH.
SRCREV = "1c0eed785c2724cf53a37f61341575eb6cbb40b6"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 3.2.0-32, so anything lower
# would look like a downgrade to opkg on an update.
PV = "3.2.0-33"

PR = "r43"

inherit webos_component
inherit webos_cmake
inherit webos_system_bus
inherit webos_daemon
inherit webos_library
inherit webos_prerelease_dep

EXTRA_OECMAKE += "-DWEBOS_DB8_BACKEND:STRING='leveldb;sandwich' -DCMAKE_SKIP_RPATH:BOOL=TRUE"
EXTRA_OECMAKE:append:class-target = " -DWEBOS_CONFIG_BUILD_TESTS:BOOL=TRUE  -DUSE_PMLOG:BOOL=TRUE  -DBUILD_LS2:BOOL=TRUE -DWANT_PROFILING:BOOL=${@ 'true' if '${WEBOS_DISTRO_PRERELEASE}' != '' else 'false'}"
EXTRA_OECMAKE:append:class-native = " -DWEBOS_CONFIG_BUILD_TESTS:BOOL=FALSE -DUSE_PMLOG:BOOL=FALSE -DBUILD_LS2:BOOL=FALSE"

WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

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

FILES:${PN}-tests = "${libdir}/${BPN}/tests"
FILES:${PN} += "${webos_sysbus_datadir}"

BBCLASSEXTEND = "native"
