FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://squashfs.cfg \
    file://video.cfg \
"

# From Yocto 2.6 to support 4.19 kernel

# for ncurses tests
inherit pkgconfig

LINUX_VERSION = "4.19"

SRCREV_machine = "122d4689678825ca23257d7a17f8da55aca8d444"
SRCREV_meta = "57b791cb9f80e89846213c42e5ababd5e7db3404"
 
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
