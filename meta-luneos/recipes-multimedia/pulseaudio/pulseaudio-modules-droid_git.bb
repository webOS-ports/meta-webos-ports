SUMMARY = "PulseAudio Droid HAL modules (Android 10 and older)"
DESCRIPTION = "The jb2q variant of the mer-hybris droid modules, covering \
Android 4.1 to 10 - despite the repository name, droidian/pulseaudio-modules-droid \
is the fork of mer-hybris/pulseaudio-modules-droid-jb2q. Android 11 and newer \
are served by the recipe next to this one, which builds the -modern fork. \
Machines pick one with PREFERRED_VERSION_pulseaudio-modules-droid."
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio pulseaudio-pulsecore-private-headers libhybris virtual/android-headers dbus udev libevdev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PV = "17.0.88+git"
SRCREV = "894f8da11f8335b09e336c599affbfc7d5fab536"

SRC_URI = "git://github.com/droidian/pulseaudio-modules-droid.git;branch=bookworm;protocol=https \
    file://0001-module-droid-use-PA_MAJORMINOR-as-PA_MODULE_VERSION-.patch \
    file://0001-conversion-split-XML-list-values-on-spaces-as-well-a.patch \
"

EXTRA_OECONF = "--with-module-dir=${libdir}/pulseaudio/modules"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

FILES:${PN} += "${libdir}/pulseaudio/modules/*.so"
FILES:${PN}-dev += "${libdir}/pulseaudio/modules/*.la"
FILES:${PN}-staticdev += "${libdir}/pulseaudio/modules/*.a"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM:${PN} = "-a pulse -g audio -G input"
