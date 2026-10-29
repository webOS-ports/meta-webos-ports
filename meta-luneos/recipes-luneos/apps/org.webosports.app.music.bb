SUMMARY = "Music: the songs on the device by album, artist, genre, folder and playlist, with Now Playing and exhibition"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PV = "1.0.0+git"
SRCREV = "9014b0c17e156c6d36b0f5bdd1031eda39d849c1"

# The QML plugin (MusicApp) links TagLib for tags and covers.
DEPENDS = "qtbase qtdeclarative qtdeclarative-native taglib"

inherit webos_ports_repo
WEBOS_GIT_PARAM_BRANCH = "main"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_application
inherit webos_filesystem_paths

inherit pkgconfig
inherit qt6-cmake
inherit webos_cmake

# The QML plugin (MusicApp) sits in the app's own directory.
INSANE_SKIP:${PN} = "libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

FILES:${PN} += " \
    ${webos_applicationsdir}/org.webosports.app.music \
    ${datadir}/luna-service2/roles.d/org.webosports.app.music.app.json \
    ${datadir}/luna-service2/manifests.d/org.webosports.app.music.manifest.json \
    ${datadir}/luna-service2/client-permissions.d/org.webosports.app.music.perm.json \
"

# The QML modules it imports: QtQuick, QtCore, Layouts and Controls
# (qtdeclarative-qmlplugins), QtMultimedia, QOfono (libqofono, to pause for
# calls; loaded optionally), Eos.Window (qml-webos-framework), and
# LuneOS.Components (ActionSheet, FastScroller), LuneOS.Forms,
# LuneOS.Service, LuneOS.System, LunaNext.Common and the LuneOS Controls
# style (luneos-components).
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    qtmultimedia-qmlplugins \
    qml-webos-framework \
    luneos-components \
"
# libqofono to pause for calls; luneos-synergy-music for the streaming
# services (signed in through Accounts), which the app finds at run time
# and does without.
RRECOMMENDS:${PN} = "libqofono luneos-synergy-music"

# What plays the formats it lists (appinfo.json mimeTypes) through Qt
# Multimedia's GStreamer backend: playbin and typefinding, conversion and
# pulsesink; MP3 (mpg123, id3demux, apetag), AAC/M4A (isomp4, libav),
# FLAC, Ogg Vorbis and Opus, WAV, and the parsers. Recommendations: a
# missing one only leaves that format unplayable ("Resource cannot be
# discovered" on a mindphone image without -flac).
RRECOMMENDS:${PN} += " \
    gstreamer1.0-plugins-base-playback \
    gstreamer1.0-plugins-base-typefindfunctions \
    gstreamer1.0-plugins-base-audioconvert \
    gstreamer1.0-plugins-base-audioresample \
    gstreamer1.0-plugins-base-volume \
    gstreamer1.0-plugins-good-pulseaudio \
    gstreamer1.0-plugins-good-mpg123 \
    gstreamer1.0-plugins-good-id3demux \
    gstreamer1.0-plugins-good-apetag \
    gstreamer1.0-plugins-good-isomp4 \
    gstreamer1.0-plugins-good-audioparsers \
    gstreamer1.0-plugins-good-flac \
    gstreamer1.0-plugins-good-wavparse \
    gstreamer1.0-plugins-base-ogg \
    gstreamer1.0-plugins-base-vorbis \
    gstreamer1.0-plugins-base-opus \
    gstreamer1.0-libav \
"

# Streams: HTTP(S) sources (souphttpsrc, and glib-networking for TLS) and
# HLS, which SoundCloud falls back to.
RRECOMMENDS:${PN} += " \
    gstreamer1.0-plugins-good-soup \
    glib-networking \
    gstreamer1.0-plugins-bad-hls \
"
