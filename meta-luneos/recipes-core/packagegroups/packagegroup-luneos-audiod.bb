SUMMARY = "LuneOS audiod stack"
DESCRIPTION = "audiod plus the PulseAudio policy module it drives. audiod claims \
com.webos.service.audio and, via palmLegacyManager, com.palm.audio and \
org.webosports.service.audio."

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = "\
    audiod \
    pulseaudio-module-palm-policy \
    com.webos.service.audiofocusmanager \
    com.webos.service.audiooutput \
"
