SUMMARY = "Sensor Framework provides access to hardware sensors and additional logical products calculated from them."
SECTION = "base"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

# We're potentially depending on libhybris so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0.14.4+git${SRCPV}"
SRCREV = "b6e7f390962e5e5fa3677a393a6fb7ba46f2d67b"
DEPENDS = "qtbase luna-sysmgr-common luna-service2 json-c glib-2.0 luna-sysmgr-ipc-messages"

SRC_URI = " \
    git://github.com/jmlich/sensorfw.git;protocol=https;branch=master \
"

S = "${WORKDIR}/git"

do_configure:prepend() {
sed "s=@LIB@=lib=g" ${S}/sensord-qt6.pc.in > ${S}/sensord-qt6.pc
}

inherit qt6-qmake
inherit pkgconfig
inherit systemd
inherit webos_system_bus
inherit webos_filesystem_paths

SERVICE_NAME = "com.nokia.SensorService"

EXTRA_QMAKEVARS_PRE += "MAKE_DOCS=no "

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
