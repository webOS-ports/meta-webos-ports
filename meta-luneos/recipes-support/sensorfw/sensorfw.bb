SUMMARY = "Sensor Framework provides access to hardware sensors and additional logical products calculated from them."
SECTION = "base"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

# We're potentially depending on libhybris so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0.15.2+git"
SRCREV = "b37c5b467e8be554066dfd1c4138dbff8eec77b3"
DEPENDS = "qtbase luna-sysmgr-common luna-service2 json-c glib-2.0 luna-sysmgr-ipc-messages"

SRC_URI = " \
    git://github.com/sailfishos/sensorfw.git;protocol=https;branch=master \
    file://0001-sensorfwd-Add-TimeoutStopSec-to-improve-shutdown.patch \
    file://0002-sensorfwd-Preload-sensors-listed-in-preload_sensors-.patch \
    file://0003-iioadaptor-Accept-input-attributes-for-IIO_CHAN_INFO.patch \
    file://0004-qt-api-do-not-marshal-invalid-QVariant-arguments.patch \
"

do_configure:prepend() {
sed "s=@LIB@=lib=g" ${S}/sensord-qt6.pc.in > ${S}/sensord-qt6.pc
}

inherit qt6-qmake
inherit pkgconfig
inherit systemd
inherit webos_system_bus
inherit webos_filesystem_paths

SERVICE_NAME = "com.nokia.SensorService"

EXTRA_QMAKEVARS_PRE += "MAKE_DOCS=no QT6_INSTALL_LIBDIR=${QT6_INSTALL_LIBDIR} "

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/LuneOS/sysbus"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "sensorfwd.service"

do_install:append() {
    # by default, point to sensord-${MACHINE}
    install -d ${D}${sysconfdir}/sensorfw/
    ln -s sensord-${MACHINE}.conf ${D}${sysconfdir}/sensorfw/primaryuse.conf
    # .. and if a machine shipped one, copy it.
    #
    # UNPACKDIR, not WORKDIR: SRC_URI files land there now, so this test quietly
    # stopped matching and the config stopped being installed, leaving the symlink
    # above dangling. sensorfw then falls back to its defaults, which look for the
    # accelerometer on /dev/input/event*, and a PineTab2's sc7a20 is an IIO device
    # with no evdev node at all - so there is no accelerometer, no orientation, and
    # the screen never rotates, with sensorfwd running and the hardware working.
    if [ -f ${UNPACKDIR}/sensord-${MACHINE}.conf ] ; then
      install -m 0644 ${UNPACKDIR}/sensord-${MACHINE}.conf ${D}${sysconfdir}/sensorfw/
    fi
    # setup script which will fix the configuration symlink if needed
    install -d ${D}${bindir}
    install -m 0755 ${S}/config/sensord-daemon-conf-setup ${D}${bindir}
    # systemd service
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/LuneOS/systemd/sensorfwd.service ${D}${systemd_unitdir}/system

    # Install the ACG configuration
    install -d ${D}${webos_sysbus_servicedir}
    install -d ${D}${webos_sysbus_rolesdir}
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.service ${D}${webos_sysbus_servicedir}/${SERVICE_NAME}.service
    install -v -m 0644 ${WEBOS_SYSTEM_BUS_FILES_LOCATION}/${SERVICE_NAME}.role.json ${D}${webos_sysbus_rolesdir}/${SERVICE_NAME}.role.json
    rm -rf ${D}/mkspecs
}

RDEPENDS:${PN} = "bash"

FILES:${PN} = " \
    ${datadir} \
    ${bindir} \
    ${sbindir} \
    ${sysconfdir} \
    ${libdir} \
"

PACKAGES =+ "${PN}-tests"

# qmake's testcase.prf bakes -DQT_TESTCASE_BUILDDIR='"$$OUT_PWD"' into every
# target with CONFIG+=testcase, for QFINDTESTDATA to find its data at runtime.
# It is a string literal rather than debug info, so -ffile-prefix-map does not
# touch it and nothing can strip it afterwards - which is why this recipe used
# to demote buildpaths from error to warning for the whole package.
#
# Scope it instead: the test binaries are the only things carrying a build path,
# and an image has no business shipping eleven of them in /usr/bin anyway. In
# their own package they are out of the image and the check stays strict for
# everything sensorfw actually installs.
FILES:${PN}-tests = " \
    ${bindir}/*-test \
    ${bindir}/sensortestapp \
    ${bindir}/datafaker-qt6 \
    ${bindir}/sensordummyclient-qt6 \
"
INSANE_SKIP:${PN}-tests += "buildpaths"
RDEPENDS:${PN}-tests = "${PN}"
