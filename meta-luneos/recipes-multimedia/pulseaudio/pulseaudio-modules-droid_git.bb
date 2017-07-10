SUMMARY = "PulseAudio Droid HAL modules"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f294906e6e4eac9d917503a0bbd139b4"

DEPENDS += "pulseaudio libhybris virtual/android-headers dbus udev"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "10.0.59+gitr${SRCPV}"
SRCREV = "d53995823d335a06a25b98d9a7ecd25f06afbcea"

SRC_URI = "git://github.com/mer-hybris/pulseaudio-modules-droid.git;branch=master;protocol=git"

SRC_URI += " \
    file://0001-add-support-for-detected-external-connection-changes.patch \
    file://0002-droid-extcon-c-adapt-to-new-pulseaudio-5-apis.patch \
    file://0003-use-pa-card-put-for-pulseaudio-8.patch \
"

S = "${WORKDIR}/git"

CFLAGS_append_tenderloin = " -DQCOM_DIRECTTRACK"

EXTRA_OECONF += " \
    --with-droid-device=${MACHINE} \
    --enable-udev \
"

# inherit webos_ports_fork_repo
inherit autotools pkgconfig

do_configure_prepend() {
  echo "${PV}" > ${S}/.tarball-version
}

FILES_${PN} += "${libdir}/pulse-10.0/modules/*.so"
FILES_${PN}-dev += "${libdir}/pulse-10.0/modules/*.la"
FILES_${PN}-staticdev += "${libdir}/pulse-10.0/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-10.0/modules/.debug"

# Add pulse user to audio group so he can access audio dev nodes from Android
GROUPMEMS_PARAM_${PN} = "-a pulse -g audio"
