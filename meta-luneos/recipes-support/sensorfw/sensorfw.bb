SUMMARY = "Sensor Framework provides access to hardware sensors and additional logical products calculated from them."
SECTION = "base"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

# We're depending on libhybris so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0.8.5+gitr${SRCPV}"
SRCREV = "e1739873c3f88170961a2e9606dacb936c6c115e"
DEPENDS = "qtbase libhybris virtual/android-headers"

SRC_URI = " \
    git://github.com/mer-packages/sensorfw;branch=master;protocol=git \
    file://0001-Replace-android-headers-hard-coded-include-path-with.patch \
    file://0002-sensorfwd.service-Change-wanted-target-to-multi-user.patch \
"

S = "${WORKDIR}/git"

inherit qmake5
inherit systemd

EXTRA_QMAKEVARS_PRE = "MAKE_DOCS=no"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/rpm/sensorfwd.service ${D}${systemd_unitdir}/system
}

FILES_${PN}-dbg += " \
  ${libdir}/sensord-qt5/testing/.debug \
  ${libdir}/sensord-qt5/.debug \
"

FILES_${PN} = " \
  ${datadir} \
  ${bindir} \
  ${sbindir} \
  ${sysconfdir} \
  ${libdir} \
  ${systemd_unitdir} \
"
