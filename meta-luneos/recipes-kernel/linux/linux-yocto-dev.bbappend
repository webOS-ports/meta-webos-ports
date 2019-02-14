FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://squashfs.cfg \
    file://video.cfg \
"

# From Yocto 2.6 to support 4.19 kernel

# for ncurses tests
inherit pkgconfig

LINUX_VERSION = "5.0~rc6"

SRCREV_machine = "8b7d7ef74a0756d511c81f0aa06fe215d8192b6c"
SRCREV_meta = "e721b5d6abf197422e8f69f2300b36b4918073ba"
 
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
