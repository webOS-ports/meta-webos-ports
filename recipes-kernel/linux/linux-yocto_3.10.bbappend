# Copyright (c) 2013 LG Electronics, Inc.

# Assign to PE, because empty PKGE in KERNEL_IMAGE_BASE_NAME causes two hyphens.
PE = "1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_qemuall = " file://my_gfx.cfg \
                           file://enable_uinput.cfg \
                         "

# replace meta/cfg/kernel-cache/cfg/sound.cfg with new sound.cfg 
#do_patch_append_qemux86() {
#    cp -v -f ${S}/../sound.cfg ${S}/meta/cfg/kernel-cache/cfg/sound.cfg
#}

