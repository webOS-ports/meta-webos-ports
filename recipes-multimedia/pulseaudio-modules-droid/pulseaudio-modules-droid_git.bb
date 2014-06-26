SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio virtual/libhardware virtual/android-headers dbus udev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "4.0+gitr${SRCPV}"

SRC_URI = "git://github.com/webOS-ports/pulseaudio-modules-droid;protocol=git;branch=webOS-ports/master"
S = "${WORKDIR}/git"

# Latest revision compatible with pulseaudio 4.0
SRCREV = "482905442fc1d175e1bcd6b2fee103bfe2c836d2"

EXTRA_OECONF += " \
    --with-droid-device=${MACHINE} \
    --enable-udev \
"

inherit autotools

FILES_${PN} += "${libdir}/pulse-4.0/modules/*.so"
FILES_${PN}-dev += "${libdir}/pulse-4.0/modules/*.la"
FILES_${PN}-staticdev += "${libdir}/pulse-4.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-4.0/modules/.debug"
