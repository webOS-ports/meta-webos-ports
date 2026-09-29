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
"

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

### Make the sensor chain libraries findable by the dynamic linker ###
#
# The chain libraries (libmagcalibrationchain-qt6.so and friends) install into
# ${libdir}/sensord-qt6 next to the plugins, and the plugins link against them by
# soname with no RPATH/RUNPATH. That directory is not on the linker's search path,
# so loading libcompasschain-qt6.so fails:
#
#     Plugin loading error: "compasschain" - "Cannot load library
#     /usr/lib/sensord-qt6/libcompasschain-qt6.so: libmagcalibrationchain-qt6.so:
#     cannot open shared object file: No such file or directory"
#     plugin marked invalid:  "compasschain"
#     plugin marked invalid:  "rotationsensor"
#
# rotationsensor is what QtSensors clients ask for, so Messwerk reported no
# rotation while the UI still rotated - the UI reads orientation/accelerometer,
# which loads fine, so this stayed hidden.
#
# Verified on pinephone 2026-09-25: creating this file and running ldconfig puts
# libmagcalibrationchain-qt6.so in the cache and the load error goes away.
# ${sysconfdir}/ld.so.conf on these images already has
# "include /etc/ld.so.conf.d/*.conf", but the directory itself did not exist.
#
# FILES:${PN} is set explicitly in the recipe, so the new file has to be added to
# it or it will not be packaged.
do_install:append() {
    install -d ${D}${sysconfdir}/ld.so.conf.d
    echo "${libdir}/sensord-qt6" > ${D}${sysconfdir}/ld.so.conf.d/sensorfw.conf
}

FILES:${PN} += "${sysconfdir}/ld.so.conf.d/sensorfw.conf"
