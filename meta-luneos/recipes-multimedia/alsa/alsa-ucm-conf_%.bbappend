FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Add-UCM-configuration-for-Nexus-4-Mako.patch \
    file://0002-Add-UCM-config-file-for-Galaxy-Nexus-maguro.patch \
    file://0003-Add-UCM-files-for-Nexus-7.patch \
    file://0005-Add-UCM-for-PinePhonePro.patch \
    file://0007-Fix-UCM-for-RK817.patch \
    git://github.com/msm8953-mainline/alsa-ucm-conf.git;protocol=https;branch=master;name=msm8953;destsuffix=${BP}/msm8953 \
"
# Was b6860eae577ddea6f55834850fb4ed403f208d2f: force-pushed away on this
# actively-rebased fork (git ls-remote now shows only one ref, master, at a
# different tip - the old commit isn't even a dangling object anymore).
# Bumped to current master tip; verified the directory layout do_install
# expects (ucm2/codecs/msm8953-wcd/, ucm2/Xiaomi/{mido,vince,daisy}/HiFi.conf,
# ucm2/conf.d/xiaomi-*/*.conf) is unchanged at this commit.
SRCREV_msm8953 = "c842a671ef37d876d8f1bd70801906c6b7eceb51"

do_install:append() {
    # msm8953: generic codecs
    install -d ${D}${datadir}/alsa/ucm2/codecs/msm8953-wcd
    install -m 0644 ${S}/msm8953/ucm2/codecs/msm8953-wcd/*.conf ${D}${datadir}/alsa/ucm2/codecs/msm8953-wcd/

    # mido
    install -d ${D}${datadir}/alsa/ucm2/Xiaomi/mido
    install -m 0644 ${S}/msm8953/ucm2/Xiaomi/mido/HiFi.conf ${D}${datadir}/alsa/ucm2/Xiaomi/mido/HiFi.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-mido
    install -m 0644 ${S}/msm8953/ucm2/conf.d/xiaomi-mido/xiaomi-mido.conf ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-mido/xiaomi-mido.conf

    # rosy/vince
    install -d ${D}${datadir}/alsa/ucm2/Xiaomi/vince
    install -m 0644 ${S}/msm8953/ucm2/Xiaomi/vince/HiFi.conf ${D}${datadir}/alsa/ucm2/Xiaomi/vince/HiFi.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-rosy
    install -m 0644 ${S}/msm8953/ucm2/conf.d/xiaomi-rosy/xiaomi-rosy.conf ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-rosy/xiaomi-rosy.conf

    # tissot/daisy
    install -d ${D}${datadir}/alsa/ucm2/Xiaomi/daisy
    install -m 0644 ${S}/msm8953/ucm2/Xiaomi/daisy/HiFi.conf ${D}${datadir}/alsa/ucm2/Xiaomi/daisy/HiFi.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-tissot
    install -m 0644 ${S}/msm8953/ucm2/conf.d/xiaomi-tissot/xiaomi-tissot.conf ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-tissot/xiaomi-tissot.conf
}
