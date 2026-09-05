# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "webOS portability layer - libhybris based modules"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

#libcrypto is a requirement and provided by openssl
DEPENDS = "nyx-lib glib-2.0 libhybris libsuspend virtual/android-headers openssl"

# The GPS module talks to the GNSS HIDL service on /dev/hwbinder rather than
# going through libhybris: the legacy gps.h HAL that hw_get_module() can reach
# only exists pre-Treble. See the note at the top of src/gps/gnss_binder.c.
DEPENDS += "libgbinder libglibutil"

# droidmedia would be needed here if any machine handed the torch to the hybris
# side (NYXMOD_OW_LEDTORCH FALSE), since that module is built around
# droid_media_camera_set_torch_mode(). No machine does, and halium-arm64 - the
# obvious candidate - deliberately does not: that symbol exists only in the 16.0
# GSI's libdroidmedia.so, and which GSI a device runs is a property of the image
# flashed to it rather than of the machine it was built from. See the reasoning in
# nyx-modules/halium-arm64.cmake. Add the dependency back, scoped to the machine,
# if one ever does: the cmake requires droidmedia only inside that branch, so
# configure fails clearly if this is forgotten.

# We need to be ${MACHINE_ARCH} as we need to compile the source against a specific
# Android version we select per machine
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Let us fetch the machine-specific CMake configuration used by nyx-modules, to
# define it only once
FILESEXTRAPATHS:prepend := "${THISDIR}/../../recipes-webos-ose/nyx-modules/nyx-modules:"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PV = "0.1.0-1+git"
PR = "r10"
SRCREV = "e56077c6932dba23fd9fd1d9f3b4f8c6272a0e1b"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

SRC_URI:append = " \
    file://${MACHINE}.cmake \
"

do_configure:prepend() {
    # Install additional machine specific nyx configuration before CMake is started
    if [ -f ${UNPACKDIR}/${MACHINE}.cmake ]
    then
        cp ${UNPACKDIR}/${MACHINE}.cmake ${S}/machine.cmake
    fi
}

FILES:${PN} += "${libdir}/nyx/modules/*"
