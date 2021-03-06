# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "webOS portability layer - libhybris based modules"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

#libcrypto is a requirement and provided by openssl
DEPENDS = "nyx-lib glib-2.0 libhybris libsuspend virtual/android-headers openssl"

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PV = "0.1.0-1+git${SRCPV}"
SRCREV = "c0c91e550d78c5c5a53412da7e6674b87e5580ee"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

FILES_${PN} += "${libdir}/nyx/modules/*"
