# This is DISTRO decision to use more optimized feeds for supported MACHINEs
# than default armv7a-neon
# it's always compromise between higher optimalization and sharing feed
# (building once for multiple MACHINES)
# For more information and benchmark read this thread:
# http://mailman.openwebosproject.org/pipermail/openwebos-general/2012-November/000156.html
# http://mailman.openwebosproject.org/pipermail/openwebos-general/2012-December/000159.html
# http://mailman.openwebosproject.org/pipermail/openwebos-general/2013-January/000177.html
# https://docs.google.com/spreadsheet/ccc?key=0Avaa_xKWksbWdDBoWjU2LU1FTVFiZDJuZTlGNkgzcFE

# Always check if recipes-core/meta/distro-feed-configs.bbappend includes all
# feeds for given DEFAULTTUNE when changing this file

# If you're changing this file then:
# build image and reflash or at least rebuild opkg-config-base distro-feed-configs
# and opkg -force-reinstall opkg-config-base distro-feed-configs
# on target to get new /etc/opkg/arch.conf

# DEFAULTTUNE:tuna = "cortexa8t-neon"
# DEFAULTTUNE:grouper = "cortexa8t-neon"

# or if you don't care about sharing binary feed between armv7a machines, 
# add this to local.conf after that require line
# DEFAULTTUNE:tuna = "cortexa9t-neon"

# A500 is using cortexa9, because Tegra2 does not support neon, so we cannot share
# feed with it unless we disable neon also for tuna:
# DEFAULTTUNE_a500 = "cortexa9"
# DEFAULTTUNE:tuna = "cortexa9"
# but be aware that grouper doesn't have cortexa9 available and a500 does not have cortexa8
# if you want to use cortexa8 for all add cortexa8 settings to a500 (see tuna.conf).

ARM_INSTRUCTION_SET = "thumb"
DEFAULTTUNE:tenderloin-halium = "cortexa8thf-neon"
DEFAULTTUNE:hammerhead-halium = "cortexa8thf-neon"
DEFAULTTUNE:mako = "cortexa8thf-neon"
DEFAULTTUNE:athene = "cortexa8thf-neon"
DEFAULTTUNE:onyx = "cortexa8thf-neon"

# http://lists.openembedded.org/pipermail/openembedded-core/2019-April/281328.html
# make sure that raspberrypi[34]-64 originally with
# DEFAULTTUNE="cortexa53"/DEFAULTTUNE="cortexa72"
# uses different TUNE_PKGARCH
# than e.g. tissot with DEFAULTTUNE="aarch64"
# it's not sure if crypto will actually work with cortexa53/BCM2837 used
# in raspberrypi3 B, see last comments in:
# https://www.raspberrypi.org/forums/viewtopic.php?t=207888
# about the license for Cryptography Extension, but hopefully the software
# built with crypto enabled will correctly check ID_AA64ISAR0_EL1 before actually using it.
DEFAULTTUNE:raspberrypi3-64 = "armv8a-crc-crypto"
TUNE_PKGARCH_64:raspberrypi3-64 = "aarch64-rpi"
PACKAGE_EXTRA_ARCHS:append:raspberrypi3-64 = " aarch64-rpi"
DEFAULTTUNE:raspberrypi4-64 = "armv8a-crc-crypto"
TUNE_PKGARCH_64:raspberrypi4-64 = "aarch64-rpi"
PACKAGE_EXTRA_ARCHS:append:raspberrypi4-64 = " aarch64-rpi"
