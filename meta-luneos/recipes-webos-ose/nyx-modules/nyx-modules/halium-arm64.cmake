# @@@LICENSE
#
#      Copyright (c) 2010-2019 LG Electronics, Inc.
#      Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# LICENSE@@@


# configuration file for halium-arm64 - every arm64 Treble device
#
# This is the generic Halium machine, so nothing here may name a device. In
# particular there are no add_definitions() overrides for input or sysfs paths:
# a device that does not match the compiled-in defaults has to be handled by the
# runtime device-config service, not by a second copy of this file. If you find
# yourself wanting to add one, that is the signal the derivation is missing.

set(NYXMOD_OW_MSMMTP					TRUE)
set(NYXMOD_OW_BATTERY					TRUE)
set(NYXMOD_OW_CHARGER					TRUE)
set(NYXMOD_OW_KEYS						TRUE)
set(NYXMOD_OW_TOUCHPANEL				FALSE)
set(NYXMOD_OW_TOUCHPANEL_MTDEV			TRUE)

# Haptics from nyx-modules rather than nyx-modules-hybris. These flags say which
# provider owns a module and nyx-modules-hybris inverts them, building haptics
# only when this is false. Its haptics module wants the free functions
# vibrator_exists()/vibrator_on()/vibrator_off() from
# <android/hardware_legacy/vibrator.h>, and that header is not in the extracted
# android-headers for 9.0, 11.0 or 13.0 - checked, not assumed - because
# hardware/libhardware_legacy stopped shipping it. With implicit declarations now
# an error, letting hybris own haptics fails the build outright.
set(NYXMOD_OW_HAPTICS					TRUE)

# provided by nyx-modules-hybris, which asks the Android HALs at runtime and so
# is device-agnostic by construction - the preferred side for anything that can
# live there.
set(NYXMOD_OW_DEVICEINFO				FALSE)
set(NYXMOD_OW_SYSTEM					FALSE)
set(NYXMOD_OW_LED						FALSE)

# GPS likewise. nyx-modules' GPS module drives a serial NMEA receiver off a
# port named in /etc/location/gpsConfig.conf, which is a PinePhone arrangement;
# a Treble device has no such port. nyx-modules-hybris binds the GNSS HIDL
# service on /dev/hwbinder instead, and negotiates the interface version at
# runtime because that version follows the flashed GSI rather than this machine
# - the same reason the torch above cannot be decided here.
set(NYXMOD_OW_GPS						FALSE)

# Left unset - the sysfs torch from nyx-modules - and that is now a decision
# rather than a placeholder.
#
# Handing it to the hybris camera-service backend reads like the natural choice on
# a Treble device, and the plan here was to do exactly that once droidmedia stopped
# pinning the old sailfishos revision. Hardware says no. The backend needs
# droid_media_camera_set_torch_mode, which only the 16.0 GSI's libdroidmedia.so
# exports, and the GSI is a property of the image flashed to a device, not of this
# machine: tissot, an arm64 machine built from here, runs the Android 9 GSI, whose
# libdroidmedia.so does not export it (checked on the device, against
# droid_media_camera_connect as a control). A build-time flag cannot pick per GSI,
# so FALSE here would take the torch away from every arm64 device not on 16.0.
#
# The premise that sysfs has little it can write did not survive contact either.
# tissot exposes led:torch_0/led:torch_1 and led:switch to this side, and the sysfs
# backend drives them: nyx-test-led leaves led:torch_0 at 200 with led:switch at 1
# while held, and both at 0 after. So unset is not a fallback here - it is the
# backend that actually reaches the hardware.
#
# If a machine ever wants the camera-service route, derive one that knows it ships
# the 16.0 GSI and set FALSE there; that recipe also has to add droidmedia to
# DEPENDS, since the cmake requires it only inside that branch.
