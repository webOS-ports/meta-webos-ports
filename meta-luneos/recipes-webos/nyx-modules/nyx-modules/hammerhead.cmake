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

# configuration file for hammerhead
# specify all the modules to be compiled

set(NYXMOD_OW_BATTERY					TRUE)
set(NYXMOD_OW_CHARGER					TRUE)
set(NYXMOD_OW_KEYS						TRUE)
set(NYXMOD_OW_TOUCHPANEL				FALSE)
set(NYXMOD_OW_TOUCHPANEL_MTDEV			TRUE)
set(NYXMOD_OW_LED						TRUE)
set(NYXMOD_OW_HAPTICS					TRUE)

add_definitions(-DTOUCHPANEL_DEVICE=\"/dev/input/touchscreen0\")
