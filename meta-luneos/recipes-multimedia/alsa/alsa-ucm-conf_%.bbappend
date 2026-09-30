FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Add-UCM-configuration-for-Nexus-4-Mako.patch \
    file://0005-Add-UCM-for-PinePhonePro.patch \
    file://0007-Fix-UCM-for-RK817.patch \
    git://github.com/msm8953-mainline/alsa-ucm-conf.git;protocol=https;branch=master;name=msm8953;destsuffix=${BP}/msm8953 \
    git://github.com/msm8916-mainline/alsa-ucm-conf.git;protocol=https;branch=master;name=msm8916;destsuffix=${BP}/msm8916 \
"
SRCREV_msm8953 = "ed9334bda853fe032794751c34cea03ec0d7d4eb"
# Pinned to what postmarketOS packages as soc-qcom-msm8916-ucm: these configs
# have never landed upstream, so this repo is the only source and a moving branch
# is not worth the breakage.
SRCREV_msm8916 = "3deea2860ab680c3e790442dbdf578fef152e639"

# Two named git fetches now, so the SRCREVs have to be named in the revision
# string as well or bitbake cannot tell the recipe changed.
SRCREV_FORMAT = "msm8953_msm8916"

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

    # msm8916 (Samsung Galaxy A3/A5 2015): the shared platform and codec
    # configs the machine config includes
    install -d ${D}${datadir}/alsa/ucm2/platforms/msm8916
    install -m 0644 ${S}/msm8916/ucm2/platforms/msm8916/*.conf ${D}${datadir}/alsa/ucm2/platforms/msm8916/
    install -d ${D}${datadir}/alsa/ucm2/codecs/msm8916-wcd
    install -m 0644 ${S}/msm8916/ucm2/codecs/msm8916-wcd/*.conf ${D}${datadir}/alsa/ucm2/codecs/msm8916-wcd/

    # a3-2015: ALSA looks the machine config up by card name, and
    # msm8916-samsung-a2015-common.dtsi sets model = "samsung-a2015", so the
    # directory name is what matters here - one config for A3 and A5 alike.
    install -d ${D}${datadir}/alsa/ucm2/samsung-a2015
    install -m 0644 ${S}/msm8916/ucm2/samsung-a2015/*.conf ${D}${datadir}/alsa/ucm2/samsung-a2015/

    # alsa-lib resolves a card through ucm2/conf.d/<card>/<card>.conf and this
    # repo still uses the old flat layout, so the mapping is made here. Without
    # it snd_use_case_mgr_open fails, no verb is applied, and the result is
    # silence with a card that registers and a PulseAudio that runs.
    install -d ${D}${datadir}/alsa/ucm2/conf.d/samsung-a2015
    ln -sf ../../samsung-a2015/samsung-a2015.conf \
        ${D}${datadir}/alsa/ucm2/conf.d/samsung-a2015/samsung-a2015.conf

    # tissot/daisy
    install -d ${D}${datadir}/alsa/ucm2/Xiaomi/daisy
    install -m 0644 ${S}/msm8953/ucm2/Xiaomi/daisy/HiFi.conf ${D}${datadir}/alsa/ucm2/Xiaomi/daisy/HiFi.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-tissot
    install -m 0644 ${S}/msm8953/ucm2/conf.d/xiaomi-tissot/xiaomi-tissot.conf ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-tissot/xiaomi-tissot.conf
}
