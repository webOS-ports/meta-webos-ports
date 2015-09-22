SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio libhybris virtual/android-headers dbus udev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "6.0.39+gitr${SRCPV}"
SRCREV = "f75332b539a438c1ab98b4ba0cefabf23d85480c"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

EXTRA_OECONF += " \
    --with-droid-device=${MACHINE} \
    --enable-udev \
"

inherit webos_ports_fork_repo
inherit autotools pkgconfig

FILES_${PN} += "${libdir}/pulse-6.0/modules/*.so"
FILES_${PN}-dev += "${libdir}/pulse-6.0/modules/*.la"
FILES_${PN}-staticdev += "${libdir}/pulse-6.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-6.0/modules/.debug"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM_${PN} = "-a pulse -g audio"
