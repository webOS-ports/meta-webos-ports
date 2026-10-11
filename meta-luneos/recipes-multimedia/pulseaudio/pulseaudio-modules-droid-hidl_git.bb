SUMMARY = "PulseAudio Droid HIDL module"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio pulseaudio-pulsecore-private-headers libhybris virtual/android-headers dbus audiosystem-passthrough pulseaudio-modules-droid"

RDEPENDS:${PN} += "pulseaudio-module-dbus-protocol"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PV = "1.4.0+git"
SRCREV = "96f02cfe157ab271646f37ba374e2f5be3cfdf55"

SRC_URI = "git://github.com/droidian/pulseaudio-modules-droid-hidl.git;branch=bookworm;protocol=https \
    file://0001-module-hidl-use-PA_MAJORMINOR-as-PA_MODULE_VERSION-.patch \
    file://0002-module-droid-hidl-guard-dbus_done-against-a-null-pr.patch \
"

EXTRA_OECONF = "--with-module-dir=${libdir}/pulseaudio/modules"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

FILES:${PN} += "${libdir}/pulseaudio/modules/*.so"
FILES:${PN}-dev += "${libdir}/pulseaudio/modules/*.la"
FILES:${PN}-staticdev += "${libdir}/pulseaudio/modules/*.a"

# pulse's groups (audio, and input on Halium for droid-extevdev) are set in
# pulseaudio_%.bbappend: this recipe does not inherit useradd, so a GROUPMEMS_PARAM here does
# nothing.
