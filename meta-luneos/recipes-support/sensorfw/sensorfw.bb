SUMMARY = "Sensor Framework provides access to hardware sensors and additional logical products calculated from them."
SECTION = "base"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

# We're potentially depending on libhybris so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0.11.0+git${SRCPV}"
SRCREV = "f9f5be4d377a82d19959cc21bae33aad92aed34d"
DEPENDS = "qtbase \
    luna-sysmgr-common luna-service2 json-c glib-2.0 luna-sysmgr-ipc-messages"
    
DEPENDS_append_halium = " libhybris virtual/android-headers "

SRC_URI = " \
    git://github.com/sailfish-on-dontbeevil/sensorfw.git;branch=dontbeevil \
    file://0001-Fix-pkgconfig-version.patch \
    file://0002-LuneOS-fix-systemd-service-file.patch \
    file://0003-Fix-build-with-autohybris.patch \
    file://0004-LuneOS-fix-dbus-service-file.patch \
    file://sensord-iiosensors.conf \
    file://90-dontbeevil.conf \
"

S = "${WORKDIR}/git"

inherit qmake5
inherit systemd
inherit webos_system_bus

EXTRA_QMAKEVARS_PRE += "MAKE_DOCS=no "
EXTRA_QMAKEVARS_PRE_append_halium = "CONFIG+=autohybris "

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = ""
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/LuneOS/sysbus"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "sensorfwd.service"

do_install_append() {
    install -d ${D}${sysconfdir}/sensorfw/
    install -m 0644 ${S}/config/sensord.conf ${D}${sysconfdir}/sensorfw/
    install -d ${D}${bindir}
    install -m 0755 ${S}/config/sensord-daemon-conf-setup ${D}${bindir}
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/LuneOS/systemd/sensorfwd.service ${D}${systemd_unitdir}/system
}

do_install_append_halium() {
    install -d ${D}${sysconfdir}/sensorfw/
    install -m 0644 ${S}/config/sensord-hybris.conf ${D}${sysconfdir}/sensorfw/
}

do_install_append_pinephone() {
    install -d ${D}${sysconfdir}/sensorfw/
    install -m 0644 ${WORKDIR}/sensord-iiosensors.conf ${D}${sysconfdir}/sensorfw/primaryuse.conf
    install -d ${D}${sysconfdir}/sensorfw/sensord.conf.d/
    install -m 0644 ${WORKDIR}/90-dontbeevil.conf ${D}${sysconfdir}/sensorfw/sensord.conf.d/
}

RDEPENDS_${PN} = "bash"

FILES_${PN} = " \
    ${datadir} \
    ${bindir} \
    ${sbindir} \
    ${sysconfdir} \
    ${libdir} \
"
