SUMMARY = "LuneOS audiod stack (opt-in, replaces audio-service)"
DESCRIPTION = "audiod plus the PulseAudio policy module it drives. Kept separate \
from packagegroup-luneos-extended so images can be built with either the current \
audio-service or audiod while the migration is in progress. audiod claims \
com.webos.service.audio and, via palmLegacyManager, com.palm.audio and \
org.webosports.service.audio -- the latter two are also claimed by audio-service, \
so the two must not be installed together."

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = "\
    audiod \
    pulseaudio-module-palm-policy \
    com.webos.service.audiofocusmanager \
    com.webos.service.audiooutput \
"

RCONFLICTS:${PN} = "audio-service"
