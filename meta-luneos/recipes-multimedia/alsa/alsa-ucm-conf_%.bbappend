FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Add-UCM-configuration-for-Nexus-4-Mako.patch \
    file://0005-Add-UCM-for-PinePhonePro.patch \
    file://0007-Fix-UCM-for-RK817.patch \
    git://github.com/msm8953-mainline/alsa-ucm-conf.git;protocol=https;branch=master;name=msm8953;destsuffix=${BP}/msm8953 \
    git://github.com/msm8916-mainline/alsa-ucm-conf.git;protocol=https;branch=master;name=msm8916;destsuffix=${BP}/msm8916 \
"
SRCREV_msm8953 = "ed9334bda853fe032794751c34cea03ec0d7d4eb"
# Pinned to what postmarketOS packages as soc-qcom-msm8916-ucm. Upstream
# alsa-ucm-conf keeps rewriting the UCM layout in patch releases and the
# msm8916 configs have never landed there, so this repository is the only
# source and a moving branch is not worth the breakage.
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

    # alsa-lib 1.2.x resolves a card to its UCM through ucm2/conf.d/<card>/<card>.conf
    # and will not find a config by directory name alone. The msm8916 repo at this
    # commit still uses the old flat layout and ships no conf.d entry, unlike the
    # msm8953 one above (conf.d/xiaomi-rosy/...), so the mapping has to be made here.
    #
    # Without it alsa-lib fails with
    #     snd_use_case_mgr_open: failed to import samsung-a2015 use case configuration -2
    # and the result is silence with a completely healthy-looking stack: the card
    # registers, the UCM files are installed, PulseAudio runs - and no verb is ever
    # applied, so nothing routes to the speaker amp.
    install -d ${D}${datadir}/alsa/ucm2/conf.d/samsung-a2015
    ln -sf ../../samsung-a2015/samsung-a2015.conf \
        ${D}${datadir}/alsa/ucm2/conf.d/samsung-a2015/samsung-a2015.conf

    # tissot/daisy
    install -d ${D}${datadir}/alsa/ucm2/Xiaomi/daisy
    install -m 0644 ${S}/msm8953/ucm2/Xiaomi/daisy/HiFi.conf ${D}${datadir}/alsa/ucm2/Xiaomi/daisy/HiFi.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-tissot
    install -m 0644 ${S}/msm8953/ucm2/conf.d/xiaomi-tissot/xiaomi-tissot.conf ${D}${datadir}/alsa/ucm2/conf.d/xiaomi-tissot/xiaomi-tissot.conf
}
