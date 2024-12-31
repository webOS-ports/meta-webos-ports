#Mer uses their own version of MBPI which has some elements added. So we want to use their version only in case we're using their oFono version. In other cases where we use upstream oFono we want to use the regular MBPI from Yocto.

SUMMARY = "Mobile Broadband Service Provider Database"
HOMEPAGE = "http://live.gnome.org/NetworkManager/MobileBroadband/ServiceProviders"
DESCRIPTION = "Mobile Broadband Service Provider Database stores service provider specific information. When this Database is available the information can be fetched there"
SECTION = "network"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://COPYING;md5=87964579b2a8ece4bc6744d2dc9a8b04"

PV = "20131125"
PE = "1"

inherit autotools

DEPENDS += "libxslt-native"

SRC_URI  = "git://github.com/sailfishos/mobile-broadband-provider-info.git;protocol=https;branch=master"
S = "${WORKDIR}/git/mobile-broadband-provider-info"

SRCREV = "4a1617295360c10331e748e3a9f42311fbe3325c"

FILES:${PN} += "${datadir}/mobile-broadband-provider-info"
