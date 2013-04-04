# Copyright (c) 2012-2013 Hewlett-Packard Development Company, L.P.
# Copyright (c) 2013 LG Electronics, Inc.

SUMMARY = "webOS portability layer - device-specific modules"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib glib-2.0 luna-service2 openssl"
DEPENDS += "mtdev"

VBOX_RDEPENDS = ""
VBOX_RDEPENDS_qemux86 = "vboxguestdrivers"
RDEPENDS_${PN} = "lsb ${VBOX_RDEPENDS}"

# corresponds to tag submissions/89
SRCREV = "95c182da4860e7e448f73c87dbdc760e3300627f"
PV = "5.1.0-89"

EXTRA_OECMAKE += "-DDISTRO_VERSION:STRING='${DISTRO_VERSION}' -DDISTRO_NAME:STRING='${DISTRO_NAME}' \
                  -DWEBOS_DISTRO_API_VERSION:STRING='${WEBOS_DISTRO_API_VERSION}' \
                  -DWEBOS_DISTRO_RELEASE_CODENAME:STRING='${WEBOS_DISTRO_RELEASE_CODENAME}' \
                  -DWEBOS_DISTRO_BUILD_ID:STRING='${WEBOS_DISTRO_BUILD_ID}'"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_library
inherit webos_machine_dep
inherit webos_machine_impl_dep
inherit webos_core_os_dep

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "daab3e726eb1512a124f9cc6a2a9bbd0968531ba"

PACKAGES += "${PN}-tests"
FILES_${PN} += "${libdir}/nyx/modules/*"
FILES_${PN}-dbg += "${libdir}/nyx/modules/.debug/*"
FILES_${PN}-tests += "${bindir}/nyx-test-ledcontroller"
