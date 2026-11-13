SUMMARY = "Padded gralloc for Waydroid's software rendering"
DESCRIPTION = "gralloc.waydroid.so, installed into Waydroid's vendor overlay: \
Android's default gralloc, with render targets allocated as tall as Mesa's \
llvmpipe assumes they are. Without it, devices with no GPU render node - \
which render Android through llvmpipe - lose SurfaceFlinger, and every \
Android app on screen, whenever a window's height is not a multiple of 4."
HOMEPAGE = "https://webos-ports.org"
SECTION = "webos/support"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
    file://gralloc_waydroid.c \
    file://bionic-stubs.c \
"

S = "${UNPACKDIR}"

# This is an Android library, not a LuneOS one: it is loaded by Android's
# linker inside the container, against the container's bionic. So the target
# compiler and C library are of no use, and none of the default dependencies
# are wanted. Clang can target Android directly; the bionic libraries it links
# against are stand-ins built here (bionic-stubs.c), since only their names
# and symbols end up in the result.
INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "clang-native lld-native"

# The Android ABI matching the host, which is the one Waydroid's images are
# built for: an arm64 host runs waydroid_arm64, and so on. API level 29 is
# the oldest any current Waydroid image is; nothing here needs a newer one.
ANDROID_TRIPLE = ""
ANDROID_TRIPLE:aarch64 = "aarch64-linux-android29"
ANDROID_TRIPLE:arm = "armv7a-linux-androideabi29"
ANDROID_TRIPLE:x86-64 = "x86_64-linux-android29"
ANDROID_TRIPLE:x86 = "i686-linux-android29"
ANDROID_LIBDIR = "lib"
ANDROID_LIBDIR:aarch64 = "lib64"
ANDROID_LIBDIR:x86-64 = "lib64"
COMPATIBLE_HOST = "(aarch64|arm|x86_64|i.86).*-linux.*"

# Waydroid mounts overlay/vendor over the vendor image (mount_overlays, its
# default), so this is /vendor/<libdir>/hw/gralloc.waydroid.so to Android.
WAYDROID_OVERLAY = "${localstatedir}/lib/waydroid/overlay"
GRALLOC_DIR = "${WAYDROID_OVERLAY}/vendor/${ANDROID_LIBDIR}/hw"

ANDROID_CC = "${STAGING_BINDIR_NATIVE}/clang --target=${ANDROID_TRIPLE} \
    -fuse-ld=lld -nostdlib -shared -fPIC"

do_configure[noexec] = "1"

do_compile() {
    mkdir -p ${B}/stubs
    for l in c dl log; do
        ${ANDROID_CC} -Wl,-soname,lib$l.so -DSTUB_LIB_$l \
            -o ${B}/stubs/lib$l.so ${S}/bionic-stubs.c
    done
    # max-page-size 16384 keeps it loadable on 16K-page Android builds too.
    ${ANDROID_CC} -O2 -ffreestanding -fvisibility=hidden -fno-stack-protector \
        -Wall -Wextra -Werror \
        -Wl,-soname,gralloc.waydroid.so -Wl,-z,defs -Wl,--hash-style=both \
        -Wl,-z,max-page-size=16384 -Wl,--build-id=sha1 \
        -o ${B}/gralloc.waydroid.so ${S}/gralloc_waydroid.c \
        -L${B}/stubs -lc -ldl -llog
}

do_install() {
    install -D -m 0644 ${B}/gralloc.waydroid.so ${D}${GRALLOC_DIR}/gralloc.waydroid.so
}

FILES:${PN} = "${GRALLOC_DIR}"

# Built without debug info and for another C library, so there is nothing for
# the host's strip and debug splitting to do, and its libc.so, libdl.so and
# liblog.so are Android's, which no LuneOS package provides.
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
SKIP_FILEDEPS:${PN} = "1"
INSANE_SKIP:${PN} += "libdir file-rdeps"
