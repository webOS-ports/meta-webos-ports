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

# DEFAULTTUNE_tuna = "cortexa8t-neon"
# DEFAULTTUNE_grouper = "cortexa8t-neon"

# or if you don't care about sharing binary feed between armv7a machines, 
# add this to local.conf after that require line
# DEFAULTTUNE_tuna = "cortexa9t-neon"

# A500 is using cortexa9, because Tegra2 does not support neon, so we cannot share
# feed with it unless we disable neon also for tuna:
# DEFAULTTUNE_a500 = "cortexa9"
# DEFAULTTUNE_tuna = "cortexa9"
# but be aware that grouper doesn't have cortexa9 available and a500 does not have cortexa8
# if you want to use cortexa8 for all add cortexa8 settings to a500 (see tuna.conf).
