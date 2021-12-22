SUMMARY = "PulseAudio Droid HIDL module"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio pulseaudio-pulsecore-private-headers libhybris virtual/android-headers dbus audiosystem-passthrough pulseaudio-modules-droid pulseaudio-module-dbus-protocol"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PULSEAUDIO_VERSION = "14.2"

PV = "${PULSEAUDIO_VERSION}.84+git${SRCPV}"
SRCREV = "ab03d444dbf44523233b0cf33b21bc58d0b1ffe8"

SRC_URI = "git://github.com/droidian/pulseaudio-modules-droid-hidl.git;branch=bookworm;protocol=https \
"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--with-module-dir=/usr/lib/pulse-${PULSEAUDIO_VERSION}/modules/"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

do_configure:prepend() {
  echo "${PV}" > ${S}/.tarball-version
}

FILES:${PN} += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.so"
FILES:${PN}-dev += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.la"
FILES:${PN}-staticdev += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.a"
