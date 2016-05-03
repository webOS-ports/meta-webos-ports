SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio libhybris virtual/android-headers dbus udev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "8.0.47+gitr${SRCPV}"
SRCREV = "4b01e83ceb8d04b9dbbc971d403674d36cce131c"

SRC_URI = "git://github.com/mer-hybris/pulseaudio-modules-droid.git;branch=master;protocol=git"

SRC_URI += " \
    file://0001-add-support-for-detected-external-connection-changes.patch \
    file://0002-droid-extcon-c-adapt-to-new-pulseaudio-5-apis.patch \
    file://0003-remove-audio-bit-in-for-mako.patch \
    file://0004-use-pa-card-put-for-pulseaudio-8.patch \
"

S = "${WORKDIR}/git"

EXTRA_OECONF += " \
    --with-droid-device=${MACHINE} \
    --enable-udev \
"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

FILES_${PN} += "${libdir}/pulse-8.0/modules/*.so"
FILES_${PN}-dev += "${libdir}/pulse-8.0/modules/*.la"
FILES_${PN}-staticdev += "${libdir}/pulse-8.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-8.0/modules/.debug"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM_${PN} = "-a pulse -g audio"
