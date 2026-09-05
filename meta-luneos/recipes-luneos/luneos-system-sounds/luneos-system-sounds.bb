SUMMARY = "LuneOS system sounds"
DESCRIPTION = "The encoded system sound set formerly shipped by luna-sysmgr. \
/usr/palm/sounds is hardcoded by its consumers: cardshell (boot, shutdown, \
battery alerts), luna-init's default ringtone/alerttone/notificationtone \
preferences, and luna-systemui (charging). Distinct from webos-systemsounds, \
the raw .pcm UI-feedback set in /usr/share/systemsounds that audiod plays."
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0+git"
SRCREV = "9432a54434b07605901adb7abe20ad51bd03be3f"

inherit webos_ports_repo
inherit webos_filesystem_paths
inherit allarch

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {
    install -d ${D}${webos_soundsdir}
    install -v -m 644 ${S}/*.mp3 ${S}/*.wav ${D}${webos_soundsdir}/
}

FILES:${PN} += "${webos_soundsdir}"
