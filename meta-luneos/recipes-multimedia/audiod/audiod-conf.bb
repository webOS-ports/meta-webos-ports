DESCRIPTION = "Platform specific configuration files for audiod"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PV = "1.0"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit webos_filesystem_paths

# Which sound card audiod looks for and what it does with it. The config audiod
# itself ships names the OSE reference board's cards - b1, b2, Headphones - which
# match nothing on any real device, so audiod finds no card, registers no device,
# and module-palm-policy is never told where to send anything. Every stream then
# plays into a virtual sink and is discarded, with no error anywhere: the only
# trace is "Invalid max device count(0)" from PulseAudio.
#
# Kept out of the audiod recipe so that adding or changing a machine's audio
# configuration does not rebuild audiod itself, the same split
# luna-surfacemanager-conf uses.
#
# Two topologies cover nearly every machine, so they are selected by override
# rather than written out per device:
#
#   halium  - the Android HAL owns the card and PulseAudio already has a sink
#             for it (module-droid-card), so audiod registers it without
#             loading anything of its own.
#   default - nothing loads a sink, so audiod creating one is correct.
#
# Both use the "*" card entry, which matches whichever internal card was found.
# Naming the card only carries information where a machine has more than one and
# the wrong one could win; demanding it everywhere else would mean no machine
# makes a sound until somebody boots it and reads the name out of a log.
SRC_URI = "file://audiod_internal_device_loading_alsa.json"
SRC_URI:halium = "file://audiod_internal_device_loading_droid.json"

# pinetab2 has a codec and an HDMI output - one of the machines where the card
# does have to be named.
SRC_URI:pinetab2 = "file://audiod_internal_device_loading.json"

AUDIOD_DEVICE_CONFIG = "audiod_internal_device_loading_alsa.json"
AUDIOD_DEVICE_CONFIG:halium = "audiod_internal_device_loading_droid.json"
AUDIOD_DEVICE_CONFIG:pinetab2 = "audiod_internal_device_loading.json"

do_install() {
    install -d ${D}${webos_sysconfdir}/audiod
    install -m 0644 ${UNPACKDIR}/${AUDIOD_DEVICE_CONFIG} \
        ${D}${webos_sysconfdir}/audiod/audiod_internal_device_loading.json
}

FILES:${PN} += "${webos_sysconfdir}/audiod"
