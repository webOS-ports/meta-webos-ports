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

# configuration file for a500.
# specify all the modules to be compiled

set(NYXMOD_OW_KEYS						TRUE)
set(NYXMOD_OW_TOUCHPANEL				TRUE)

# provided by nyx-modules-hybris
set(NYXMOD_OW_DEVICEINFO				FALSE)
set(NYXMOD_OW_SYSTEM					FALSE)
set(NYXMOD_OW_LED						FALSE)
set(NYXMOD_OW_HAPTICS					FALSE)

add_definitions(-DKEYPAD_INPUT_DEVICE=\"/dev/input/by-path/platform-gpio-keys.0-event\")
add_definitions(-DBATTERY_SYSFS_PATH=\"/sys/class/power_supply/battery/\")
add_definitions(-DBACKLIGHT_SYSFS_PATH=\"/sys/class/backlight/pwm-backlight/\")
