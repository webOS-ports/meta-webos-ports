# Copyright (c) 2013-2025 LG Electronics, Inc.

DESCRIPTION = "System Application Manager"
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson boost icu pmloglib libwebosi18n gtest"
RDEPENDS:${PN} = "ecryptfs-utils"
RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_webos-customization}"

VIRTUAL-RUNTIME_webos-customization ?= ""

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: what used to be the
# 0001-0011 patch series plus the icu-75 C++17 fix in this directory now lives
# as commits there, along with the static-analysis cleanup on top of it, so
# nothing is applied here any more. Pinned with a plain
# SRCREV - submission tags are a webosose convention and this branch carries
# none. The branch itself comes from webos_ports_ose_repo below.
SRCREV = "bd2c65204cbc8fb9050a593db22f8363f8913806"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 2.0.0-77, so anything lower
# would look like a downgrade to opkg on an update.
PV = "2.0.0-81"

PR = "r39"

inherit webos_component
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
#inherit webos_distro_variant_dep
# The warning cleanup and test harness work (webOS-ports/sam#8) lives on
# herrie/cleanup-test-hardening, not on the branch webos_ports_ose_repo
# defaults to, so the SRCREV above is not reachable from
# webOS-ports/webOS-OSE. Drop this line once that branch is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/cleanup-test-hardening"
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "sam.service"
WEBOS_SYSTEMD_SCRIPT = "sam.sh"

PACKAGECONFIG:append = " ${@bb.utils.filter('DISTRO_FEATURES', 'smack', d)}"
PACKAGECONFIG[smack] = "-Dapply_webos_smack:BOOL=True"

# Build and package the gtest suite under tests/. INSTALL_TESTS implies
# BUILD_TESTS; without either, tests/CMakeLists.txt is a no-op and gtest is
# only a build-time dependency.
EXTRA_OECMAKE += "-DWEBOS_CONFIG_INSTALL_TESTS:BOOL=TRUE"

PACKAGES =+ "${PN}-tests"
ALLOW_EMPTY:${PN}-tests = "1"
FILES:${PN}-tests = "${webos_testsdir}/* ${libexecdir}/tests/*"
