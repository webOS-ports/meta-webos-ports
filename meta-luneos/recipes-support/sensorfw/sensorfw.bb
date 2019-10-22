SUMMARY = "Sensor Framework provides access to hardware sensors and additional logical products calculated from them."
SECTION = "base"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

# We're potentially depending on libhybris so need to be MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "0.11.1+git${SRCPV}"
SRCREV = "92b9c6f715e0b1a3db151e0b532e634818b89058"
DEPENDS = "qtbase \
    luna-sysmgr-common luna-service2 json-c glib-2.0 luna-sysmgr-ipc-messages"
    
DEPENDS_append_halium = " libhybris virtual/android-headers "

SRC_URI = " \
    git://github.com/sailfish-on-dontbeevil/sensorfw.git;branch=dontbeevil \
    file://0002-LuneOS-fix-systemd-service-file.patch \
    file://0003-Fix-build-with-autohybris.patch \
    file://0004-LuneOS-fix-dbus-service-file.patch \
"

# Note: maybe this should go in a bbappend in meta-pine64-luneos...
SRC_URI_append_pinephone = " \
    file://sensord-pinephone.conf \
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
}

do_install_append_halium() {
    install -d ${D}${sysconfdir}/sensorfw/
    install -m 0644 ${S}/config/sensord-hybris.conf ${D}${sysconfdir}/sensorfw/
}

RDEPENDS_${PN} = "bash"

FILES_${PN} = " \
    ${datadir} \
    ${bindir} \
    ${sbindir} \
    ${sysconfdir} \
    ${libdir} \
"
