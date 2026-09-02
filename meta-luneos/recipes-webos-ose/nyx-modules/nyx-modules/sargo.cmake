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

# configuration file for rosy
# specify all the modules to be compiled

set(NYXMOD_OW_MSMMTP					TRUE)
set(NYXMOD_OW_BATTERY					TRUE)
set(NYXMOD_OW_CHARGER					TRUE)
set(NYXMOD_OW_KEYS						TRUE)
set(NYXMOD_OW_TOUCHPANEL				FALSE)
set(NYXMOD_OW_TOUCHPANEL_MTDEV			TRUE)

# provided by nyx-modules-hybris
set(NYXMOD_OW_DEVICEINFO				FALSE)
set(NYXMOD_OW_SYSTEM					FALSE)
set(NYXMOD_OW_LED						FALSE)
# Haptics has to come from nyx-modules, not nyx-modules-hybris. The hybris
# module calls the free functions vibrator_exists()/vibrator_on()/vibrator_off()
# from <android/hardware_legacy/vibrator.h>, and that header is not in the
# extracted android-headers for any version we ship, because
# hardware/libhybris_legacy stopped providing it. With implicit declarations now
# an error, FALSE here fails the nyx-modules-hybris build outright. Same
# reasoning, and the same value, as halium-arm64.cmake.
set(NYXMOD_OW_HAPTICS					TRUE)
