SUMMARY = "PulseAudio Droid HAL modules (Android 11 and newer)"
DESCRIPTION = "The mer-hybris droid modules for Android 11 and newer, as forked \
by Droidian. The other recipe next to this one builds the jb2q variant of the \
same modules, which covers Android 4.1 to 10; upstream splits them because the \
audio HAL and its configuration changed too much to keep in one tree. Machines \
pick one with PREFERRED_VERSION_pulseaudio-modules-droid."
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

# libtool for libltdl and expat are new next to the jb2q recipe: the meson
# build looks both up directly (cc.find_library("ltdl"), dependency("expat")).
DEPENDS += "pulseaudio pulseaudio-pulsecore-private-headers libhybris virtual/android-headers dbus udev libevdev expat libtool"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

# Upstream numbers these <pulseaudio major.minor>.<module version>, so this is
# module version 103 built against PulseAudio 17.0. Keeping that scheme means
# the version says which PulseAudio the modules will actually load into - see
# the .tarball-version note below.
MODULE_VERSION = "17.0.103"
PV = "${MODULE_VERSION}+git"
SRCREV = "935726a4390dd61a81336a4185ed7fc4ea915699"

FILESEXTRAPATHS:prepend := "${THISDIR}/pulseaudio-modules-droid-modern:"

SRC_URI = "git://github.com/droidian/pulseaudio-modules-droid-modern.git;branch=droidian;protocol=https \
    file://0001-modules-declare-the-PulseAudio-version-we-build-agai.patch \
    file://0002-droid-util-fall-back-to-set_parameters-without-audio.patch \
"

inherit meson pkgconfig

# Upstream's autotools --with-module-dir.
EXTRA_OEMESON = "-Dmodlibexecdir=${libdir}/pulseaudio/modules"

# meson.build gets the project version by running git-version-gen, which wants
# either a .tarball-version or a git checkout with matching tags. Neither is
# guaranteed here, and without one the configure aborts outright, so write the
# file ourselves the way the upstream deb and rpm packaging both do.
do_configure:prepend() {
    echo "${MODULE_VERSION}" > ${S}/.tarball-version
}

FILES:${PN} += "${libdir}/pulseaudio/modules/*.so"
FILES:${PN}-dev += "${libdir}/pulseaudio/modules/*.la"
FILES:${PN}-staticdev += "${libdir}/pulseaudio/modules/*.a"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM:${PN} = "-a pulse -g audio -G input"
