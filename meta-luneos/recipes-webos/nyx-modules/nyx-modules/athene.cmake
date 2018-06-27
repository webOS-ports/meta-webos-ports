# @@@LICENSE
#
#      Copyright (c) 2010-2012 Hewlett-Packard Development Company, L.P.
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

# configuration file for athene
# specify all the modules to be compiled

set(MODULE_SYSTEM_WEBOS_LINUX		NO)
set(MODULE_BATTERY_WEBOS_LINUX		YES)
set(MODULE_CHARGER_WEBOS_LINUX		YES)
set(MODULE_KEYS_WEBOS_LINUX			YES)
set(MODULE_TOUCHPANEL_WEBOS_LINUX		NO)
set(MODULE_TOUCHPANEL_MTDEV_WEBOS_LINUX		YES)

add_definitions(-DBATTERY_SYSFS_PATH=\"/sys/class/power_supply/battery/\")
add_definitions(-DTOUCHPANEL_DEVICE=\"/dev/input/event1\")
add_definitions(-DCHARGER_AC_SYSFS_PATH=\"/sys/class/power_supply/ac/\")
