SUMMARY = "Sensors HAL for the Waydroid container, backed by sensorfw"
DESCRIPTION = "Answers android.hardware.sensors@1.0::ISensors on the host side of \
    the Waydroid hwbinder, reading the real sensors through sensorfw. Without it \
    Waydroid sets waydroid.stub_sensors_hal=1 and Android gets no accelerometer, \
    gyroscope, magnetometer, proximity or light data at all - screen rotation \
    included."
HOMEPAGE = "https://github.com/waydroid/waydroid-sensors"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SECTION = "webos/support"

# No tags upstream; this is main as of 2026-09-02.
SRCREV = "2f4791750b6b634aa0d52e38355d7c77a0694700"
PV = "0.2.0+git"
PR = "r0"

SRC_URI = "git://github.com/waydroid/waydroid-sensors.git;branch=main;protocol=https \
    file://waydroid-sensors.service \
"

# The RPM packaging carried over from Sailfish also asks for Qt5Core and
# Qt5Network. Nothing in the tree includes a Qt header - sensorfw is spoken to
# over its own socket and D-Bus protocol through sensorfw-core - so that
# dependency is stale and is deliberately not carried here, which matters
# because LuneOS is on Qt6.
DEPENDS = "glib-2.0 libglibutil libgbinder"

# Same machines as waydroid itself: the daemon is only useful with a container
# to answer, and needs the anbox-* binder nodes that come with it.
COMPATIBLE_MACHINE ?= "(^$)"
COMPATIBLE_MACHINE:mido-halium = "(.*)"
COMPATIBLE_MACHINE:tissot-halium = "(.*)"
COMPATIBLE_MACHINE:mindphone = "(.*)"
COMPATIBLE_MACHINE:halium-arm64 = "(.*)"
COMPATIBLE_MACHINE:pinephone = "(.*)"
COMPATIBLE_MACHINE:pinephonepro = "(.*)"
COMPATIBLE_MACHINE:pinetab2 = "(.*)"
COMPATIBLE_MACHINE:qemux86-64 = "(.*)"

inherit cmake pkgconfig webos_systemd

# Upstream still asks for cmake_minimum_required(VERSION 3.0), which CMake 4
# refuses outright rather than warning about. Nothing in the project depends on
# the old policy behaviour - it is one executable and one static library - so
# raise the floor here instead of carrying a patch for an otherwise untouched
# tree.
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"



WEBOS_SYSTEMD_SERVICE = "waydroid-sensors.service"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/waydroid-sensors.service ${D}${systemd_system_unitdir}
}

RDEPENDS:${PN} += "sensorfw"
