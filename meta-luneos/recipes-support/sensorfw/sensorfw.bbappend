FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Devices specific configuration and options for sensorfw go here

### Halium devices related configuration ###
DEPENDS:append:halium = " libhybris virtual/android-headers libgbinder libglibutil "

do_install:append:halium() {
    install -d ${D}${sysconfdir}/sensorfw/
    install -m 0644 ${S}/config/sensord-hybris.conf ${D}${sysconfdir}/sensorfw/
}

EXTRA_QMAKEVARS_PRE:append:halium = "CONFIG+=autohybris "
EXTRA_QMAKEVARS_PRE:append:halium = "CONFIG+=luneos "

# Halium-9.0 devices use binder to communicate with sensors
EXTRA_QMAKEVARS_PRE:append:halium = "CONFIG+=binder "

# Tenderloin here is an exception: sensorfw doesn't need to use Halium for the sensor
EXTRA_QMAKEVARS_PRE:remove:tenderloin-halium = "CONFIG+=autohybris "
SRC_URI:append:tenderloin-halium = " \
    file://sensord-tenderloin-halium.conf \
    file://90-tenderloin-halium-sensors.rules \
"

do_install:append:tenderloin-halium() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/90-tenderloin-halium-sensors.rules ${D}${sysconfdir}/udev/rules.d/
}

### Mainline devices related configuration ###
SRC_URI:append:tenderloin = " \
    file://sensord-tenderloin.conf \
"

# tenderloin71 is the upstream-kernel build variant of the same board (same
# sensor layout). Stage the same config so the iiosensorsadaptor path
# actually gets installed there too — without this append the
# sensord-tenderloin71.conf in the layer was never picked up by the build.
# The ISL29023 cover-glass calibration lives in the kernel device tree
# (isil,cover-comp-gain) rather than a udev rule, so this recipe carries
# nothing user-side for it.
SRC_URI:append:tenderloin71 = " \
    file://sensord-tenderloin71.conf \
"

SRC_URI:append:hammerhead = " \
    file://sensord-hammerhead.conf \
"

SRC_URI:append:rosy = " \
    file://sensord-rosy.conf \
"

SRC_URI:append:tissot = " \
    file://sensord-tissot.conf \
"

SRC_URI:append:a3-2015 = " \
    file://sensord-a3-2015.conf \
"

### Put the sensor chain libraries on the linker path ###
#
# They install into ${libdir}/sensord-qt6 beside the plugins, which link them by
# soname with no RPATH, so compasschain and rotationsensor both failed to load on
# libmagcalibrationchain-qt6.so and were marked invalid. rotationsensor is what
# QtSensors clients ask for, so Messwerk saw no rotation while the UI still
# rotated. ld.so.conf already includes ld.so.conf.d/*.conf; the directory did not
# exist. FILES:${PN} is explicit in the recipe, so the new file must be added.
do_install:append() {
    install -d ${D}${sysconfdir}/ld.so.conf.d
    echo "${libdir}/sensord-qt6" > ${D}${sysconfdir}/ld.so.conf.d/sensorfw.conf
}

FILES:${PN} += "${sysconfdir}/ld.so.conf.d/sensorfw.conf"
