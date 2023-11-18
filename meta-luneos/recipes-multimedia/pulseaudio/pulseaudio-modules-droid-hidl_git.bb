SUMMARY = "PulseAudio Droid HIDL module"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio pulseaudio-pulsecore-private-headers libhybris virtual/android-headers dbus audiosystem-passthrough pulseaudio-modules-droid"

RDEPENDS:${PN} += "pulseaudio-module-dbus-protocol"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PULSEAUDIO_VERSION = "15.0"

PV = "1.4.0+git${SRCPV}"
SRCREV = "96f02cfe157ab271646f37ba374e2f5be3cfdf55"

SRC_URI = "git://github.com/droidian/pulseaudio-modules-droid-hidl.git;branch=bookworm;protocol=https \
    file://0001-module-hidl-use-PA_MAJORMINOR-as-PA_MODULE_VERSION-.patch \
"

S = "${WORKDIR}/git"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

FILES:${PN} += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.so"
FILES:${PN}-dev += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.la"
FILES:${PN}-staticdev += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.a"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM:${PN} = "-a pulse -g audio -G input"
