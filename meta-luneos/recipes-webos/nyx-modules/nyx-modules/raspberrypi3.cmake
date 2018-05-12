# @@@LICENSE
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

# configuration file for raspberrypi3
# specify all the modules to be compiled

set(NYXMOD_OW_SYSTEM		NO)
set(NYXMOD_OW_BATTERY		NO)
set(NYXMOD_OW_CHARGER		NO)
set(NYXMOD_OW_KEYS			YES)
set(NYXMOD_OW_TOUCHPANEL		 NO)
set(NYXMOD_OW_TOUCHPANEL_MTDEV		YES)

add_definitions(-DTOUCHPANEL_DEVICE=\"/dev/input/event0\")
