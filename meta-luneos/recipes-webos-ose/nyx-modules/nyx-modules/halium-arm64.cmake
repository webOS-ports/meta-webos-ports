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
