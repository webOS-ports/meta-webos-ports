SUMMARY = "Sound Recorder: records from the microphone, plays, renames, sends and deletes recordings"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "1.0.0+git"
SRCREV = "91cf81f51634cfe453ee3516863984d8a60516ed"

DEPENDS = "qtbase qtdeclarative qtdeclarative-native qtmultimedia"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit webos_application
inherit webos_filesystem_paths

inherit qt6-cmake
inherit webos_cmake

# The QML plugin (SoundRecorderApp) sits in the app's own directory.
INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += " \
    ${webos_applicationsdir}/org.webosports.app.soundrecorder \
    ${datadir}/luna-service2/roles.d/org.webosports.app.soundrecorder.app.json \
    ${datadir}/luna-service2/manifests.d/org.webosports.app.soundrecorder.manifest.json \
    ${datadir}/luna-service2/client-permissions.d/org.webosports.app.soundrecorder.perm.json \
"

# The QML modules it imports: QtQuick, QtCore, Layouts and Controls
# (qtdeclarative-qmlplugins), QtMultimedia, Eos.Window
# (qml-webos-framework), and LuneOS.Components (ArtworkImage), LuneOS.Forms,
# LuneOS.Service, LuneOS.System, LunaNext.Common and the LuneOS Controls
# style (luneos-components).
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    qtmultimedia-qmlplugins \
    qml-webos-framework \
    luneos-components \
"

# The GStreamer elements its formats are written with; the app offers only
# the formats Qt Multimedia finds it can write, so each is a recommendation.
# encodebin, audioconvert/audioresample and pulsesrc for every recording;
# lamemp3enc (MP3), libav's avenc_aac and mp4mux (AAC), vorbisenc and opusenc
# with oggmux (Ogg Vorbis, Opus), flacenc (FLAC), wavenc (WAV).
RRECOMMENDS:${PN} = " \
    gstreamer1.0-plugins-base-encoding \
    gstreamer1.0-plugins-base-audioconvert \
    gstreamer1.0-plugins-base-audioresample \
    gstreamer1.0-plugins-good-pulseaudio \
    gstreamer1.0-plugins-good-lame \
    gstreamer1.0-libav \
    gstreamer1.0-plugins-good-isomp4 \
    gstreamer1.0-plugins-base-ogg \
    gstreamer1.0-plugins-base-vorbis \
    gstreamer1.0-plugins-base-opus \
    gstreamer1.0-plugins-good-flac \
    gstreamer1.0-plugins-good-wavenc \
"
