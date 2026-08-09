# @@@LICENSE
#
#      Copyright (c) 2010-2019 LG Electronics, Inc.
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

# configuration file for tenderloin
# specify all the modules to be compiled

set(NYXMOD_OW_BATTERY					TRUE)
set(NYXMOD_OW_CHARGER					TRUE)
set(NYXMOD_OW_KEYS						TRUE)
set(NYXMOD_OW_TOUCHPANEL				FALSE)
set(NYXMOD_OW_TOUCHPANEL_MTDEV			TRUE)
set(NYXMOD_OW_HAPTICS					TRUE)
set(NYXMOD_OW_DEVICEINFO				TRUE)
set(NYXMOD_OW_SYSTEM					TRUE)
set(NYXMOD_OW_LED						TRUE)


# HP TouchPad has two A6 microcontrollers on i2c-2 (0x31 and 0x32).
# a6-0 is the battery fuel-gauge/charger MCU; a6-1 is the second Touchstone
# coil's A6 used by the legacy webOS tap2shared daemon for Touch-to-Share.
# Both register as power_supply type=Battery (the kernel driver doesn't
# distinguish their roles), so without an explicit pin here the
# directory-walk auto-detection can pick a6-1, which reports zero
# everywhere and leaves the cardshell battery indicator stuck at 0%.
add_definitions(-DBATTERY_SYSFS_PATH=\"/sys/class/power_supply/a6-0\")

# MAX8903B charger appears as a "Mains" type power_supply. Pin the path so
# chargerd doesn't race the directory walk against ci_hdrc_usb.
add_definitions(-DCHARGER_AC_SYSFS_PATH=\"/sys/class/power_supply/max8903_charger\")
add_definitions(-DCHARGER_USB_SYSFS_PATH=\"/sys/class/power_supply/ci_hdrc_usb\")

# CY8CTMA395 touchscreen is /dev/input/event2 on the mainline kernel.
# Legacy LuneOS-on-Halium expected event6 — keep TOUCHPANEL_DEVICE
# accurate for the running kernel so nyxTouchpanelMain doesn't fall back
# to scanning by name.
add_definitions(-DTOUCHPANEL_DEVICE=\"/dev/input/event2\")
