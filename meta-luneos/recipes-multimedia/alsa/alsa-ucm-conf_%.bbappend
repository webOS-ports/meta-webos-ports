FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Add-UCM-configuration-for-Nexus-4-Mako.patch \
    file://0002-Add-UCM-config-file-for-Galaxy-Nexus-maguro.patch \
    file://0003-Add-UCM-files-for-Nexus-7.patch \
"

SRC_URI:append:pinephone = " \
    file://ucm2/PinePhone.conf \
    file://ucm2/HiFi \
    file://ucm2/VoiceCall \
"

SRC_URI:append:pinephonepro = " \
    file://ucm2/PinePhonePro.conf \
    file://ucm2/HiFi.conf \
    file://ucm2/VoiceCall.conf \
"

SRC_URI:append:pinetab2 = " \
    file://ucm2/PineTab2.conf \
    file://ucm2/HiFi.conf \
    file://ucm2/VoiceCall.conf \
"

do_install:append:pinephone() {
    install -d ${D}${datadir}/alsa/ucm2/PinePhone
    install -m 0644 ${WORKDIR}/ucm2/PinePhone.conf ${D}${datadir}/alsa/ucm2/PinePhone/PinePhone.conf
    install -m 0644 ${WORKDIR}/ucm2/HiFi ${D}${datadir}/alsa/ucm2/PinePhone/HiFi
    install -m 0644 ${WORKDIR}/ucm2/VoiceCall ${D}${datadir}/alsa/ucm2/PinePhone/VoiceCall

    install -d ${D}${datadir}/alsa/ucm2/conf.d/simple-card
    ln -s /usr/share/alsa/ucm2/PinePhone/PinePhone.conf ${D}${datadir}/alsa/ucm2/conf.d/simple-card/PinePhone.conf
}

do_install:append:pinephonepro() {
    install -d ${D}${datadir}/alsa/ucm2/PinePhonePro
    install -d ${D}${datadir}/alsa/ucm2/Pine64/PinePhonePro
    install -m 0644 ${WORKDIR}/ucm2/PinePhonePro.conf ${D}${datadir}/alsa/ucm2/Pine64/PinePhonePro/PinePhonePro.conf
    install -m 0644 ${WORKDIR}/ucm2/HiFi.conf ${D}${datadir}/alsa/ucm2/Pine64/PinePhonePro/HiFi.conf
    install -m 0644 ${WORKDIR}/ucm2/VoiceCall.conf ${D}${datadir}/alsa/ucm2/Pine64/PinePhonePro/VoiceCall.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/simple-card
    ln -sf ../../Pine64/PinePhonePro/PinePhonePro.conf ${D}${datadir}/alsa/ucm2/conf.d/simple-card/PinePhonePro.conf
}

do_install:append:pinetab2() {
    install -d ${D}${datadir}/alsa/ucm2/PineTab2
    install -d ${D}${datadir}/alsa/ucm2/Pine64/PineTab2
    install -m 0644 ${WORKDIR}/ucm2/PineTab2.conf ${D}${datadir}/alsa/ucm2/Pine64/PineTab2/PineTab2.conf
    install -m 0644 ${WORKDIR}/ucm2/HiFi.conf ${D}${datadir}/alsa/ucm2/Pine64/PineTab2/HiFi.conf
    install -m 0644 ${WORKDIR}/ucm2/VoiceCall.conf ${D}${datadir}/alsa/ucm2/Pine64/PineTab2/VoiceCall.conf
    install -d ${D}${datadir}/alsa/ucm2/conf.d/simple-card
    ln -sf ../../Pine64/PineTab2/PineTab2.conf ${D}${datadir}/alsa/ucm2/conf.d/simple-card/PineTab2.conf
}
