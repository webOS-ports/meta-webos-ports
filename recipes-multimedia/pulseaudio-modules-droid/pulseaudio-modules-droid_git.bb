SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio virtual/libhardware virtual/android-headers dbus udev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "5.0.16+gitr${SRCPV}"
SRCREV = "cb861412b41bfdcbdac2e021c208808b1fcda986"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

EXTRA_OECONF += " \
    --with-droid-device=${MACHINE} \
    --enable-udev \
"

inherit webos_ports_repo
inherit autotools pkgconfig

FILES_${PN} += "${libdir}/pulse-5.0/modules/*.so"
FILES_${PN}-dev += "${libdir}/pulse-5.0/modules/*.la"
FILES_${PN}-staticdev += "${libdir}/pulse-5.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-5.0/modules/.debug"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM_${PN} = "-a pulse -g audio"
