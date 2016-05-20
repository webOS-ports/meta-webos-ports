SUMMARY = "Sensor Framework provides access to hardware sensors and additional logical products calculated from them."
SECTION = "base"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

# We're depending on libhybris so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0.8.5+gitr${SRCPV}"
SRCREV = "a4bf550865f64ec4ac3c1a4d880a9dd7b37d4053"
DEPENDS = "qtbase libhybris virtual/android-headers \
    luna-sysmgr-common luna-service2 json-c glib-2.0 luna-sysmgr-ipc-messages"

SRC_URI = " \
    git://github.com/mer-packages/sensorfw;branch=master;protocol=git \
    file://0001-Replace-android-headers-hard-coded-include-path-with.patch \
    file://0002-sensord-daemon-conf-setup-improve-check-for-libhybri.patch \
    file://0003-Set-okToStop-to-false-to-prevent-deadlock-when-stopp.patch \
    file://0004-Implement-luna-service-integration.patch \
"

S = "${WORKDIR}/git"

inherit qmake5
inherit systemd
inherit webos_system_bus

EXTRA_QMAKEVARS_PRE = "MAKE_DOCS=no"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/LuneOS/sysbus"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "sensorfwd.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/LuneOS/systemd/sensorfwd.service ${D}${systemd_unitdir}/system
}

RDEPENDS_${PN} = "bash"

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
"
