SUMMARY = "Fixed Mesa svga driver for the Waydroid container on VMware SVGA"
DESCRIPTION = "Android build of Mesa 23.2.1's vmwgfx_dri.so with two svga fixes, \
put into Waydroid's vendor overlay so Android apps can render on the GPU \
under VirtualBox (VMSVGA) and VMware."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# The image's own vmwgfx_dri.so makes every Android app abort on its first
# frame, and fixing that alone leaves windows black outside the region they
# redrew last; waydroid-luneos-prepare.sh has the details and falls back to
# SwiftShader when this package is not installed.
#
# This is a prebuilt, kept in this layer, because it is an Android (bionic)
# library: the Yocto toolchain cannot produce it. build-vmwgfx-dri.sh next to
# it builds it from the Mesa 23.2.1 release, libdrm 2.4.120 and NDK r26d with
# the two patches there, and gives the same file bit for bit from any
# directory, so the checksum do_install checks can be verified by rebuilding. The patches are
# submitted to Mesa; this goes away once a Waydroid image ships a Mesa that
# has them.
#
# The driver has to come from the same Mesa release as the image's
# libEGL_mesa.so, which loads it: 23.2.1 is what
# lineage-18.1-20231028-VANILLA-waydroid_x86_64 (waydroid-data.bb) carries. A
# newer x86-64 image means rebuilding this from that image's Mesa.
COMPATIBLE_MACHINE ?= "(^$)"
COMPATIBLE_MACHINE:qemux86-64 = "(.*)"

# The fetcher does not checksum file:// sources, so do_install does.
SRC_URI = "file://vmwgfx_dri-23.2.1-luneos1-x86_64.so.xz"
VMWGFX_DRI_SHA256 = "c5b3d81749c9eac682f22597a90e7a3c73852ca555ed2c5dd18089d626ae0842"

S = "${UNPACKDIR}"

do_install() {
    echo "${VMWGFX_DRI_SHA256}  ${UNPACKDIR}/vmwgfx_dri-23.2.1-luneos1-x86_64.so" |
        sha256sum -c -
    install -d ${D}${datadir}/waydroid-extra/vmwgfx
    install -m 0644 ${UNPACKDIR}/vmwgfx_dri-23.2.1-luneos1-x86_64.so \
        ${D}${datadir}/waydroid-extra/vmwgfx/vmwgfx_dri.so
}

FILES:${PN} = "${datadir}/waydroid-extra/vmwgfx"

# Loaded inside the container by bionic's linker, never on the host: its
# NEEDED entries (libc.so, libcutils.so, libsync.so, libglapi.so, ...) are Android
# libraries that no host package provides, and it is already stripped.
SKIP_FILEDEPS:${PN} = "1"
EXCLUDE_FROM_SHLIBS = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP:${PN} += "already-stripped file-rdeps"

RDEPENDS:${PN} = "waydroid"
