FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
    file://0002-Add-UCM-configuration-for-Nexus-4-Mako.patch \
    file://0003-Add-UCM-config-file-for-Galaxy-Nexus-maguro.patch \
    file://0004-Add-UCM-files-for-Nexus-7.patch \
"

# I'm not sure if we still need this one, if we do it needs to be updated
# as ALSA_USE_CASE_DIR isn't available anymore since:
# commit 516bf057b04579134ab97098ed579967427a2ede
# Author: Takashi Iwai <tiwai@suse.de>
# Date:   Wed May 3 00:09:28 2017 +0200
# conf: Allow dynamic top-level config directory
# and we cannot easily pass snd_use_case_mgr_t into filename_filter
# file://0001-ucm-fix-handling-of-config-files-when-file-type-is-n.patch
