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

# configuration file for sargo
# specify all the modules to be compiled

set(NYXMOD_OW_MSMMTP					TRUE)
set(NYXMOD_OW_BATTERY					TRUE)
set(NYXMOD_OW_CHARGER					TRUE)
set(NYXMOD_OW_KEYS						TRUE)
set(NYXMOD_OW_TOUCHPANEL				FALSE)
set(NYXMOD_OW_TOUCHPANEL_MTDEV			TRUE)
# Haptics comes from nyx-modules, not nyx-modules-hybris. These flags say which
# provider owns a module, and nyx-modules-hybris inverts them - it builds haptics
# precisely when NYXMOD_OW_HAPTICS is false:
#
#   if(NOT NYXMOD_OW_HAPTICS)
#       add_subdirectory(haptics)
#
# Its haptics module calls the free functions vibrator_exists()/vibrator_on()/
# vibrator_off() from <android/hardware_legacy/vibrator.h>, and android-headers
# 9.0 does not ship that header - Android 9 exposes only the vibrator_device_t
# HAL struct and its function pointers. So on this machine that module cannot
# compile at all, and now that GCC treats implicit declarations as errors it
# fails the build outright instead of silently producing something broken.
# tissot-halium, also Android 9, takes haptics from nyx-modules for this reason.
set(NYXMOD_OW_HAPTICS					TRUE)

# provided by nyx-modules-hybris
set(NYXMOD_OW_DEVICEINFO				FALSE)
set(NYXMOD_OW_SYSTEM					FALSE)
set(NYXMOD_OW_LED						FALSE)
