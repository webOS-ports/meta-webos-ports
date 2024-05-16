FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Add-UCM-configuration-for-Nexus-4-Mako.patch \
    file://0002-Add-UCM-config-file-for-Galaxy-Nexus-maguro.patch \
    file://0003-Add-UCM-files-for-Nexus-7.patch \
    file://0005-Add-UCM-for-PinePhonePro.patch \
    file://0007-Fix-UCM-for-RK817.patch \
    git://github.com/msm8953-mainline/alsa-ucm-conf.git;protocol=https;branch=master;name=msm8953;destsuffix=${BP}/msm8953 \
"
SRCREV_msm8953 = "b6860eae577ddea6f55834850fb4ed403f208d2f"

do_install:append() {
    # msm8953: generic codecs
    install -d ${D}${datadir}/alsa/ucm2/codecs/msm8953-wcd
    install -m 0644 ${S}/msm8953/ucm2/codecs/msm8953-wcd/*.conf ${D}${datadir}/alsa/ucm2/codecs/msm8953-wcd/

    # mido
    install -d ${D}${datadir}/alsa/ucm2/Xiaomi/mido
    install -m 0644 ${S}/msm8953/ucm2/Xiaomi/mido/HiFi.conf ${D}${datadir}/alsa/ucm2/Xiaomi/mido/HiFi.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-mido
    install -m 0644 ${S}/msm8953/ucm2/conf.d/xiaomi-mido/xiaomi-mido.conf ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-mido/xiaomi-mido.conf

    # rosy
    install -d ${D}${datadir}/alsa/ucm2/Xiaomi/vince
    install -m 0644 ${S}/msm8953/ucm2/Xiaomi/vince/HiFi.conf ${D}${datadir}/alsa/ucm2/Xiaomi/vince/HiFi.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-rosy
    install -m 0644 ${S}/msm8953/ucm2/conf.d/xiaomi-rosy/xiaomi-rosy.conf ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-rosy/xiaomi-rosy.conf
}
