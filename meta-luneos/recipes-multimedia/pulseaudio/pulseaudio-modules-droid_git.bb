SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio pulseaudio-pulsecore-private-headers libhybris virtual/android-headers dbus udev libevdev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PULSEAUDIO_VERSION = "15.0"

PV = "${PULSEAUDIO_VERSION}"
SRCREV = "80015ede829752fcb357f8bbd7b59d861c865d38"

SRC_URI = "git://github.com/droidian/pulseaudio-modules-droid.git;branch=bookworm;protocol=https \
"

S = "${WORKDIR}/git"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

do_configure:prepend() {
  echo "${PV}" > ${S}/.tarball-version
}

FILES:${PN} += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.so"
FILES:${PN}-dev += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.la"
FILES:${PN}-staticdev += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.a"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM:${PN} = "-a pulse -g audio -G input"
