# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "webOS portability layer - libhybris based modules"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib glib-2.0 libhybris libsuspend"

# For the sake of the webOS build system we need to provide the webOS component version
# and even a submission number, even if we don't use any.
WEBOS_COMPONENT_VERSION = "0.1.0"
WEBOS_SUBMISSION = "0"
PV = "${WEBOS_COMPONENT_VERSION}+gitr${SRCPV}"

inherit webos_component
inherit webos_public_repo
inherit webos_cmake
inherit webos_library

SRC_URI = "git://github.com/webOS-ports/nyx-modules-hybris;branch=master;protocol=git"
S = "${WORKDIR}/git"

SRCREV = "8d16ddcc7c8687a96c37d6c34e18f5fcfd13e0e5"

FILES_${PN} += "${libdir}/nyx/modules/*"
FILES_${PN}-dbg += "${libdir}/nyx/modules/.debug/*"
