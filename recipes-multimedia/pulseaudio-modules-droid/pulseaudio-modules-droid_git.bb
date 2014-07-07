SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio virtual/libhardware virtual/android-headers dbus udev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "4.0+gitr${SRCPV}"

SRC_URI = "git://github.com/webOS-ports/pulseaudio-modules-droid;branch=webOS-ports/master-next"
S = "${WORKDIR}/git"

SRCREV = "5eaf1405e1b48f128ffb3577bda3e7ddd0c75a8c"

EXTRA_OECONF += " \
    --with-droid-device=${MACHINE} \
    --enable-udev \
"

inherit autotools

FILES_${PN} += "${libdir}/pulse-5.0/modules/*.so"
FILES_${PN}-dev += "${libdir}/pulse-5.0/modules/*.la"
FILES_${PN}-staticdev += "${libdir}/pulse-5.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-5.0/modules/.debug"
