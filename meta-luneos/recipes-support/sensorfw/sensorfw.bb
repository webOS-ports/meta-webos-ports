SUMMARY = "Sensor Framework provides access to hardware sensors and additional logical products calculated from them."
SECTION = "base"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

# We're potentially depending on libhybris so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0.11.4+git${SRCPV}"
#PV = "0.12.6+git${SRCPV}"
#SRCREV = "2e539015996111576a731750342effde7aaee87f"
SRCREV = "381f732430c9eb041aedc039013502fb2da40a26"
DEPENDS = "qtbase luna-sysmgr-common luna-service2 json-c glib-2.0 luna-sysmgr-ipc-messages"

DEPENDS:append:halium = " libhybris virtual/android-headers libgbinder libglibutil "

SRC_URI = " \
    git://github.com/webos-ports/sensorfw.git;protocol=https;branch=herrie/qt6 \
"

# Note: maybe this should go in a bbappend in meta-pine64-luneos...
SRC_URI:append:pinephone = " \
    file://sensord-pinephone.conf \
"
SRC_URI:append:pinephonepro = " \
    file://sensord-pinephonepro.conf \
"

# Note: maybe this should go in a bbappend in meta-smartphone...
SRC_URI:append:tenderloin = " \
    file://sensord-tenderloin.conf \
"

SRC_URI:append:hammerhead = " \
    file://sensord-hammerhead.conf \
"

S = "${WORKDIR}/git"

#do_configure:prepend() {
#sed "s=@LIB@=lib=g" ${S}/sensord-qt5.pc.in > ${S}/sensord-qt5.pc
#}

inherit qt6-qmake
inherit pkgconfig
inherit systemd
inherit webos_system_bus
inherit webos_filesystem_paths

SERVICE_NAME = "com.nokia.SensorService"

EXTRA_QMAKEVARS_PRE += "MAKE_DOCS=no "
EXTRA_QMAKEVARS_PRE:append:halium = "CONFIG+=autohybris "

# Halium-9.0 devices use binder to communicate with sensors
EXTRA_QMAKEVARS_PRE:append:hammerhead-halium = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:mako = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:mido = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:rosy = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:sagit = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:tissot = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:yggdrasil = "CONFIG+=binder "

# Tenderloin here is an exception: sensorfw doesn't need to use Halium for the sensor
EXTRA_QMAKEVARS_PRE:remove:tenderloin = "CONFIG+=autohybris "

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/LuneOS/sysbus"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "sensorfwd.service"

do_install:append() {
    # by default, point to sensord-${MACHINE}
    install -d ${D}${sysconfdir}/sensorfw/
    ln -s sensord-${MACHINE}.conf ${D}${sysconfdir}/sensorfw/primaryuse.conf
    # .. and if the file is already in WORKDIR, copy it
    if [ -f ${WORKDIR}/sensord-${MACHINE}.conf ] ; then
      install -m 0644 ${WORKDIR}/sensord-${MACHINE}.conf ${D}${sysconfdir}/sensorfw/
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
}

do_install:append:halium() {
    install -d ${D}${sysconfdir}/sensorfw/
    install -m 0644 ${S}/config/sensord-hybris.conf ${D}${sysconfdir}/sensorfw/
}

RDEPENDS:${PN} = "bash"

FILES:${PN} = " \
    ${datadir} \
    ${bindir} \
    ${sbindir} \
    ${sysconfdir} \
    ${libdir} \
"
