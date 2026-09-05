SUMMARY = "LuneOS system sounds"
DESCRIPTION = "The system sound set formerly shipped by luna-sysmgr. \
/usr/palm/sounds is hardcoded by its consumers: cardshell (boot, shutdown, \
battery alerts), luna-init's default ringtone/alerttone/notificationtone \
preferences, and luna-systemui (charging)."
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0"

inherit webos_filesystem_paths
inherit allarch

SRC_URI = " \
    file://alert.wav \
    file://battery_full.mp3 \
    file://battery_low.mp3 \
    file://boot.mp3 \
    file://charging.mp3 \
    file://error.mp3 \
    file://notification.wav \
    file://panel.mp3 \
    file://phone.wav \
    file://ringtone.mp3 \
    file://shutdown.mp3 \
    file://tap_to_share.mp3 \
"

do_install() {
    install -d ${D}${webos_soundsdir}
    for f in ${UNPACKDIR}/*.mp3 ${UNPACKDIR}/*.wav; do
        install -v -m 644 $f ${D}${webos_soundsdir}/
    done
}

FILES:${PN} += "${webos_soundsdir}"
