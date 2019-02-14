FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://squashfs.cfg \
    file://video.cfg \
"

LINUX_VERSION = "5.0~rc6"

SRCREV_machine = "8b7d7ef74a0756d511c81f0aa06fe215d8192b6c"
SRCREV_meta = "e721b5d6abf197422e8f69f2300b36b4918073ba"
