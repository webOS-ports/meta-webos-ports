# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Event-driven startup scripts for system services"
SECTION = "webos/base"
LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

VIRTUAL-RUNTIME_rdx-utils ?= "rdx-utils-stub"
RDEPENDS_${PN} = "upstart ${VIRTUAL-RUNTIME_rdx-utils}"

WEBOS_VERSION = "2.0.0-123_8255bd94321e3ae79a55b7572aeac8868bd560fb"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_arch_indep

# Depends on TUNE_PKGARCH recipe upstart as detected by bitbake-diffsigs
# Hash for dependent task upstart_0.3.11.bb.do_packagedata changed from 2729d5e2d0073efd8c32bb657e04f4ed to eb389aaea92547dfc9d8e85c4a9bfae6
# But because default PACKAGE_ARCH in webOS is MACHINE_ARCH use MACHINE_ARCH here as well.
inherit webos_machine_dep

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
