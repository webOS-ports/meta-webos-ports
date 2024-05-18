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
EXTRA_QMAKEVARS_PRE:append:hammerhead-halium = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:mako = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:mido-halium = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:sagit = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:tissot = "CONFIG+=binder "
EXTRA_QMAKEVARS_PRE:append:yggdrasil = "CONFIG+=binder "

# Tenderloin here is an exception: sensorfw doesn't need to use Halium for the sensor
EXTRA_QMAKEVARS_PRE:remove:tenderloin-halium = "CONFIG+=autohybris "
SRC_URI:append:tenderloin-halium = " \
    file://sensord-tenderloin-halium.conf \
"

### Mainline devices related configuration ###
SRC_URI:append:tenderloin = " \
    file://sensord-tenderloin.conf \
"

SRC_URI:append:hammerhead = " \
    file://sensord-hammerhead.conf \
"

SRC_URI:append:hammerhead = " \
    file://sensord-rosy.conf \
"
