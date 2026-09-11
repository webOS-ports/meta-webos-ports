# @@@LICENSE
#
#      Copyright (c) 2026 LuneOS Team
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

SUMMARY = "bluejay: clear stale Trusty secure-storage files that panic the kernel"
DESCRIPTION = "Trusty's own storage app hits a block-cache consistency \
assertion on two specific pre-existing backing files and treats that as \
fatal to the whole secure OS, which panics the normal-world kernel too. \
Moves them aside (idempotent, safe every boot) before android-system.service \
ever starts, so Trusty formats fresh storage instead. See \
bluejay-audio-blocker-SOLVED in project memory for the full trace - this was \
the actual, previously-unexplained bluejay audio blocker."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# Depends on this project's own trusty-ipc-before-trusty-virtio module
# ordering (luneos-bootimg-bluejay overlay/init-gki-modules.diff) actually
# being what brought Trusty's storage app far enough to hit this in the
# first place - meaningless on a device where Trusty is not reachable at
# all. Scoped to sargo, bluejay's build stand-in, since it has no MACHINE
# of its own yet (Priority 1 in PATCH-INVENTORY-AND-UPSTREAM-PLAN.md) -
# move this file there once it exists.
COMPATIBLE_MACHINE = "^sargo$"

S = "${UNPACKDIR}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "bluejay-trusty-storage-fixup.service"
WEBOS_SYSTEMD_SCRIPT = "bluejay-trusty-storage-fixup.sh"
