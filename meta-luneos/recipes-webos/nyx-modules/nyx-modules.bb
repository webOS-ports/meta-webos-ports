# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "webOS portability layer - ${MACHINE}-specific modules"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib glib-2.0 luna-service2 openssl"
DEPENDS += "mtdev"

VBOX_RDEPENDS = ""
VBOX_RDEPENDS_qemux86 = "vboxguestdrivers"
VBOX_RDEPENDS_qemux86-64 = "vboxguestdrivers"
RDEPENDS_${PN} = "lsb gzip ${VBOX_RDEPENDS} nyx-conf"

PV = "6.1.0-94+git${SRCPV}"
SRCREV = "e76e2b71e530649f1eb187fd3d16f20a3bb77dd6"

EXTRA_OECMAKE += "-DDISTRO_VERSION:STRING='${DISTRO_VERSION}' -DDISTRO_NAME:STRING='${DISTRO_NAME}' \
                  -DWEBOS_DISTRO_API_VERSION:STRING='${WEBOS_DISTRO_API_VERSION}' \
                  -DWEBOS_DISTRO_RELEASE_CODENAME:STRING='${WEBOS_DISTRO_RELEASE_CODENAME}' \
                  -DWEBOS_DISTRO_BUILD_ID:STRING='${WEBOS_DISTRO_BUILD_ID}'"

# Only pass in a value for the Manufacturing version if one is actually
# defined. Otherwise, let the CMake script provide the default value.
#
# Defining it to be the empty string will override the default used in
# the CMake script.
WEBOS_DISTRO_MANUFACTURING_VERSION ??= ""
EXTRA_OECMAKE += "${@ '-DWEBOS_DISTRO_MANUFACTURING_VERSION:STRING="${WEBOS_DISTRO_MANUFACTURING_VERSION}"' \
                  if d.getVar('WEBOS_DISTRO_MANUFACTURING_VERSION',True) != '' else ''}"

# NB. CMakeLists.txt arranges for the return value of the NYX_OS_INFO_WEBOS_PRERELEASE
# query to be "" when WEBOS_DISTRO_PRERELEASE is not defined on the command line.
WEBOS_DISTRO_PRERELEASE ??= ""
EXTRA_OECMAKE += "${@ '-DWEBOS_DISTRO_PRERELEASE:STRING="${WEBOS_DISTRO_PRERELEASE}"' \
                  if d.getVar('WEBOS_DISTRO_PRERELEASE',True) != '' else ''}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit webos_ports_fork_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_machine_impl_dep
inherit webos_core_os_dep

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

PACKAGES += "${PN}-tests"
FILES_${PN} += "${libdir}/nyx/modules/*"
FILES_${PN}-dbg += "${libdir}/nyx/modules/.debug/*"
FILES_${PN}-tests += "${bindir}/nyx-test-ledcontroller"
