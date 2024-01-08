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
DEFAULTTUNE:tenderloin = "cortexa8thf-neon"
DEFAULTTUNE:hammerhead = "cortexa8thf-neon"
DEFAULTTUNE:mako = "cortexa8thf-neon"
DEFAULTTUNE:athene = "cortexa8thf-neon"
DEFAULTTUNE:onyx = "cortexa8thf-neon"

# in meta-smartphone/meta-android/conf/machine/include/meta-android-halium.inc
# we do:
# TUNE_PKGARCH:append = "-halium"
# PACKAGE_EXTRA_ARCHS:append = " ${TUNE_PKGARCH}"
# but when building something with thumb disabled (e.g. webruntime-108), we need to search nspr in
# cortexa8t2hf-neon-halium, not cortexa8hf-neon-halium used by webruntime, add it explicitly here, but it's far from ideal
PACKAGE_EXTRA_ARCHS_HALIUM = "${@oe.utils.conditional('ARM_INSTRUCTION_SET', 'thumb', 'cortexa8hf-neon-halium', 'cortexa8t2hf-neon-halium', d) }"
PACKAGE_EXTRA_ARCHS:append:tenderloin-halium = " ${PACKAGE_EXTRA_ARCHS_HALIUM}"
PACKAGE_EXTRA_ARCHS:append:hammerhead-halium = " ${PACKAGE_EXTRA_ARCHS_HALIUM}"

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

# Undo the switch to x86-64-v3 from:
# https://git.openembedded.org/openembedded-core/commit/?id=6f2af1e5d1537b4d31e14946292bf58f0fd76fc9
# https://git.openembedded.org/openembedded-core/commit/?id=ac041f90e71dba83b7144c91f929de88aaeae519
# because we use VirtualBox and it doesn't support fma, f16c as shown with various Qt binaries:
# root@qemux86-64:~# mediaindexer
# Incompatible processor. This Qt build requires the following features: fma f16c

# There is 7 years old enhancement ticket for Virtualbox without any progress:
# https://www.virtualbox.org/ticket/15471

# Don't switch all the way back to core2-64, corei7-64 should be fine nowadays.
# core2-64 (Intel Core2 CPU with 64-bit extensions, MMX, SSE, SSE2, SSE3 and SSSE3 instruction set support. Intel Core 2 CPU family, including Conroe, Merom and beyond, as well as the first Atom CPUs, Diamondville, and beyond)
# corei7-64 (Intel Nehalem CPU with 64-bit extensions, MMX, SSE, SSE2, SSE3, SSSE3, SSE4.1, SSE4.2 and POPCNT instruction set support. This tune is recommended for Intel Nehalem and Silvermont (e.g. Bay Trail) CPUs (and beyond)).
# x86-64-v3 (CPUs with AVX, AVX2, BMI1, BMI2, F16C, FMA, LZCNT, MOVBE, XSAVE. (but not AVX512). See https://www.phoronix.com/news/GCC-11-x86-64-Feature-Levels for details.)
DEFAULTTUNE:qemux86-64 = "corei7-64"
