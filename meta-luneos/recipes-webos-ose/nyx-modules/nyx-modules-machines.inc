# Which nyx modules the open-webos provider owns, per machine.
#
# Required by both nyx-modules and nyx-modules-hybris, because the two recipes
# have to agree: these NYXMOD_OW_* flags do not say "build this module", they
# say *which of the two providers* builds it. nyx-modules builds a module when
# its flag is set and nyx-modules-hybris builds the same module when it is not,
# so a machine the two recipes disagreed about would either ship the module
# twice - both packages installing the same nyxFoo.module and colliding - or
# not at all.
#
# This replaces the per-machine src/machine.cmake that nyx-modules used to
# unpack from file://${MACHINE}.cmake: 42 files for 37 machines that between
# them held only 8 distinct answers, and that made a new machine fail to parse
# until somebody wrote it one. NYX_MODULES_REQUIRED is the mechanism
# webos_nyx_module_provider.bbclass already provides for exactly this, and
# meta-rpi-luneos was already using it. A machine with nothing to say here now
# gets the default below rather than an error.
#
# To give a new machine its own set, override NYX_MODULES_REQUIRED in the
# machine's own .conf or a bbappend - the machine override wins over everything
# here.

# The default is a native (non-halium, non-container) device: this provider
# owns everything it can, because there is no Android side to hand anything to.
#
# ALS is in here rather than opted into per machine, which is a change: no
# native machine built it before, and several have a light sensor their sensorfw
# config already names - opt3001 on the Comet, stk3310 on rosy, isl29023 on the
# TouchPad, ltr579 on tissot.
#
# src/als/als.c finds the sensor itself by walking /sys/bus/iio/devices for
# in_illuminance_raw, so there is nothing per-device to get right here. Where
# that channel is missing it falls back to a hardcoded evdev node, fails to open
# and does not load, so default-on costs an unloaded module at worst - and a
# board whose driver later starts exposing the channel picks it up with no
# change here.
#
# hammerhead is in exactly that state and is deliberately not excluded: it has an
# apds9930, but its luna-platform.conf records that the mainline tsl2772 driver
# publishes no illuminance_raw. The module will not load there today and will
# start working if the driver ever exposes the channel, with no change here.
#
# The PineTab2 is excluded, below, for a different reason.
NYX_MODULES_REQUIRED = "\
    NYXMOD_OW_ALS;\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_HAPTICS;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_LED;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL_MTDEV;\
"

# On a halium machine deviceinfo, system, LED and GPS go to nyx-modules-hybris,
# which asks the Android HALs at runtime and is device-agnostic by
# construction - the preferred side for anything that can live there. Battery,
# charger, keys and the mtdev touchpanel stay here: they read sysfs and evdev,
# which the container does not own.
#
# Haptics stays here too, and not by preference. The hybris haptics module wants
# vibrator_exists()/vibrator_on()/vibrator_off() from
# <android/hardware_legacy/vibrator.h>, and that header is not in the extracted
# android-headers for 9.0, 11.0 or 13.0 - hardware/libhardware_legacy stopped
# shipping it. With implicit declarations an error, letting hybris own haptics
# fails the build.
#
# Six older machines still hand it over, listed individually below. That is what
# their cmake files said and it is carried across unchanged; whether they still
# build is a separate question from this refactor.
NYX_MODULES_REQUIRED:halium = "\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_HAPTICS;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_TOUCHPANEL_MTDEV;\
"

# The halium set with haptics handed to nyx-modules-hybris instead. Preserved
# from each machine's cmake file as it stood; see the note above about the
# header the hybris module needs.
NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS = "\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_TOUCHPANEL_MTDEV;\
"
NYX_MODULES_REQUIRED:athene            = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:hammerhead-halium = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:mako              = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:onyx              = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:oxygen            = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"
NYX_MODULES_REQUIRED:sagit             = "${NYX_MODULES_REQUIRED_HALIUM_NO_HAPTICS}"

# No battery and no charger on a Pi, and nothing here owns system either.
# NYXMOD_OW_CEC is appended by meta-rpi-luneos' bbappend rather than listed
# here, so the CEC module stays that layer's business.
NYX_MODULES_REQUIRED:rpi = "\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_TOUCHPANEL_MTDEV;\
"

# The PinePhones drive a serial NMEA receiver off a port named in
# /etc/location/gpsConfig.conf, so GPS is ours; a Treble device has no such port
# and uses the hybris GNSS binder module instead.
#
# ALS covers both, which it did not before: only pinephonepro had it, and the
# PinePhone's cmake file simply never picked it up when the ALS work was done on
# the Pro. Both carry a SensorTek part on I2C behind the cover glass -
# "sensortek,stk3311" at light-sensor@48 in sun50i-a64-pinephone-1.2.dts,
# stk3310 on the Pro - and the stk3310 driver binds the whole STK3310/3311/3335
# family and exposes in_illuminance_raw through IIO. That is exactly what
# src/als/als.c walks /sys/bus/iio/devices looking for, so the module needs no
# per-device configuration; the flag only decides whether it is shipped.
#
# Costless to get wrong in the other direction, too: with no IIO illuminance
# channel the module falls back to a hardcoded evdev node, fails to open and
# does not load.
NYX_MODULES_REQUIRED_PINEPHONE = "\
    NYXMOD_OW_ALS;\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_GPS;\
    NYXMOD_OW_HAPTICS;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_LED;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL_MTDEV;\
"
NYX_MODULES_REQUIRED:pinephone    = "${NYX_MODULES_REQUIRED_PINEPHONE}"
NYX_MODULES_REQUIRED:pinephonepro = "${NYX_MODULES_REQUIRED_PINEPHONE}"

# The x86 emulators are the only machines using the plain evdev touchpanel
# rather than the mtdev one, and the only ones without TOUCHPANEL_MTDEV.
NYX_MODULES_REQUIRED_QEMU_X86 = "\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL;\
"
NYX_MODULES_REQUIRED:qemux86    = "${NYX_MODULES_REQUIRED_QEMU_X86}"
NYX_MODULES_REQUIRED:qemux86-64 = "${NYX_MODULES_REQUIRED_QEMU_X86}"

# qemuarm had almost nothing in its cmake file - no battery, charger, keys or
# touchpanel of any kind - so it gets only what the class default gave it.
NYX_MODULES_REQUIRED:qemuarm = "\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
"

# The PineTab2 is the one machine taken off the ALS default, and unlike
# hammerhead - where the part exists but the driver publishes no
# illuminance_raw - this one has no sensor to drive at all.
#
# The published specs list an ambient light sensor and nobody had ever located
# it. Probed on a PineTab2 v2.0 running 7.2.0-pinetab2 (2026-09-20), which
# settles it:
#
#   - IIO exposes exactly two devices, fe720000.saradc and the sc7a20
#     accelerometer. No in_illuminance* channel anywhere.
#   - Every i2c bus scanned. Each responding address is a known device already
#     bound to a driver: 0x1c tcs4525, 0x20 rk817, 0x4e husb311 (bus 0), 0x5d
#     gt911 (bus 1), 0x0c dw9714 + 0x36 ov5648 + 0x37 gc02m2 (bus 2), 0x18
#     sc7a20 (bus 5). Not one unclaimed address on any bus.
#   - i2c@fe5c0000 and i2c@fe5d0000 are disabled, and declare no child nodes -
#     unused SoC controllers, not a sensor waiting to be switched on.
#   - Nothing in devices_deferred, nothing in dmesg.
#
# So the sensor is either not populated on v2.0 hardware or the spec sheet is
# wrong. Shipping the module would be harmless - with no in_illuminance_raw it
# fails to open and does not load - but it would assert a working ALS that is
# not there. luna-platform.conf already says EnableALS=false; this agrees.
#
# If a light sensor ever turns up, delete this block: the default already has ALS.
NYX_MODULES_REQUIRED_NO_ALS = "\
    NYXMOD_OW_BATTERY;\
    NYXMOD_OW_CHARGER;\
    NYXMOD_OW_DEVICEINFO;\
    NYXMOD_OW_DISPLAY;\
    NYXMOD_OW_HAPTICS;\
    NYXMOD_OW_KEYS;\
    NYXMOD_OW_LED;\
    NYXMOD_OW_MSMMTP;\
    NYXMOD_OW_OSINFO;\
    NYXMOD_OW_SYSTEM;\
    NYXMOD_OW_TOUCHPANEL_MTDEV;\
"
NYX_MODULES_REQUIRED:pinetab2 = "${NYX_MODULES_REQUIRED_NO_ALS}"
