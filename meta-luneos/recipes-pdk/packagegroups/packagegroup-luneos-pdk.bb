SUMMARY = "Legacy webOS PDK/SDL application support"
DESCRIPTION = "Everything needed to run the 2010-2012 native webOS game \
catalogue on LuneOS: the soft-float ARM sysroot, the Palm-API shims, and the \
launcher. Add to an image with:  IMAGE_INSTALL:append = \" packagegroup-luneos-pdk\""

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# Machine-arch, not allarch. packagegroup.bbclass defaults PACKAGE_ARCH to "all"
# unless the recipe sets it first, and an allarch recipe gets TARGET_ARCH =
# "allarch" - which makes the architecture overrides below silently never match.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    pdk-sysroot \
    pdk-tools \
    pdk-sysroot-fonts \
"

# Legacy luna-service2 (2.0.0-136) is wire-incompatible with the current one, so
# the shims shell out to luna-send rather than speaking the protocol. luna-send
# is a binary inside the luna-service2 package - there is no package by that name.
RDEPENDS:${PN} += "luna-service2"

# PDK apps expect a working PulseAudio socket. SDL_AUDIODRIVER=dummy is not an
# acceptable substitute - it turns Mix_OpenAudio failures into NULL dereferences
# somewhere unrelated. See docs/troubleshooting.md.
RDEPENDS:${PN} += "pulseaudio-server"

# The emulator only where the CPU cannot execute ARM32 itself. On ARMv7 the
# binaries run natively - softfp and hardfp are the same instruction set - and
# pdk-run execs them through the sysroot's own loader instead.
# qemu-user-arm is a dynamic package of oe-core's qemu recipe; it ships
# /usr/bin/qemu-arm, which is one of the paths pdk-run looks in.
RDEPENDS:${PN}:append:x86-64 = " qemu-user-arm"
RDEPENDS:${PN}:append:x86 = " qemu-user-arm"
RDEPENDS:${PN}:append:aarch64 = " qemu-user-arm"
