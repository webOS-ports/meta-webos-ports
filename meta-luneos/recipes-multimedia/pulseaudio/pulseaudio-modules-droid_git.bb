SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio libhybris virtual/android-headers dbus udev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# Depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

PULSEAUDIO_VERSION = "12.2"

PV = "${PULSEAUDIO_VERSION}.77+git${SRCPV}"
SRCREV = "b1ce02b3d6e983fed34bba9defea9a1d74d6b917"

SRC_URI = "git://github.com/mer-hybris/pulseaudio-modules-droid.git \
    file://0001-Add-support-for-detected-external-connection-changes.patch \
    file://0002-droid-extcon.c-adapt-to-new-pulseaudio-5-APIs.patch \
"

S = "${WORKDIR}/git"

EXTRA_OECONF += " \
    --with-droid-device=${MACHINE} \
    --enable-udev \
"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

do_configure_prepend() {
  echo "${PV}" > ${S}/.tarball-version
}

FILES_${PN} += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.so"
FILES_${PN}-dev += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.la"
FILES_${PN}-staticdev += "${libdir}/pulse-${PULSEAUDIO_VERSION}/modules/*.a"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM_${PN} = "-a pulse -g audio"
