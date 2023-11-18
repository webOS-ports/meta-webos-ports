SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio pulseaudio-pulsecore-private-headers libhybris virtual/android-headers dbus udev libevdev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PV = "15.0.88+git${SRCPV}"
SRCREV = "894f8da11f8335b09e336c599affbfc7d5fab536"

SRC_URI = "git://github.com/droidian/pulseaudio-modules-droid.git;branch=bookworm;protocol=https \
    file://0001-module-droid-use-PA_MAJORMINOR-as-PA_MODULE_VERSION-.patch \
"

S = "${WORKDIR}/git"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

FILES:${PN} += "${libdir}/pulse-*/modules/*.so"
FILES:${PN}-dev += "${libdir}/pulse-*/modules/*.la"
FILES:${PN}-staticdev += "${libdir}/pulse-*/modules/*.a"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM:${PN} = "-a pulse -g audio -G input"
