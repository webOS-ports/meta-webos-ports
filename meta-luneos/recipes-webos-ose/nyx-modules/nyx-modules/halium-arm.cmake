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


# configuration file for halium-arm - every 32-bit arm Treble device
#
# The 32-bit sibling of halium-arm64.cmake, and deliberately the same set of
# choices: every one of them is made by what the Treble/GSI split can decide at
# build time, none by the width of the pointer. Read that file for the long
# reasoning behind each flag below; this one records only where the two differ,
# which so far is nowhere.
#
# As on arm64 this is the generic Halium machine, so nothing here may name a
# device. In particular there are no add_definitions() overrides for input or
# sysfs paths - compare mindphone.cmake, which has four and is a device machine
# for exactly that reason. A device whose paths do not match the compiled-in
# defaults has to be handled by the runtime device-config service; wanting a
# line here is the signal the derivation is missing.

set(NYXMOD_OW_MSMMTP					TRUE)
set(NYXMOD_OW_BATTERY					TRUE)
set(NYXMOD_OW_CHARGER					TRUE)
set(NYXMOD_OW_KEYS						TRUE)
set(NYXMOD_OW_TOUCHPANEL				FALSE)
set(NYXMOD_OW_TOUCHPANEL_MTDEV			TRUE)

# Haptics from nyx-modules rather than nyx-modules-hybris: the hybris haptics
# module wants vibrator_exists()/vibrator_on()/vibrator_off() from
# <android/hardware_legacy/vibrator.h>, which the extracted android-headers do
# not ship at any version this machine can use. Same conclusion as
# halium-arm64.cmake and mindphone.cmake.
set(NYXMOD_OW_HAPTICS					TRUE)

# provided by nyx-modules-hybris, which asks the Android HALs at runtime and so
# is device-agnostic by construction - the preferred side for anything that can
# live there.
set(NYXMOD_OW_DEVICEINFO				FALSE)
set(NYXMOD_OW_SYSTEM					FALSE)
set(NYXMOD_OW_LED						FALSE)

# GPS likewise, and this is the one line mindphone.cmake does not carry. That is
# an omission there rather than a 32-bit difference: nyx-modules' GPS module
# drives a serial NMEA receiver off a port named in /etc/location/gpsConfig.conf,
# which is a PinePhone arrangement that no Treble device has. nyx-modules-hybris
# binds the GNSS HIDL service on /dev/hwbinder instead and negotiates the
# interface version at runtime, because that version follows the flashed GSI
# rather than this machine.
set(NYXMOD_OW_GPS						FALSE)

# NYXMOD_OW_LEDTORCH left unset - the sysfs torch from nyx-modules - for the
# reason spelled out in halium-arm64.cmake: the hybris camera-service backend
# needs droid_media_camera_set_torch_mode, which only the 16.0 GSI's
# libdroidmedia.so exports, and which GSI a device runs is a property of the
# image flashed to it, not of this machine. A build-time flag cannot pick per
# GSI, so this stays on the sysfs backend that reaches the hardware either way.
