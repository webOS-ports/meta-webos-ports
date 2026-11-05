SUMMARY = "Media engine for Teams calls"
DESCRIPTION = "teams_media carries the audio and video of a Microsoft Teams call for purple-teams' \
calling bridge: ICE with libnice, AEAD_AES_256_GCM SRTP, Opus between the network and the \
PulseAudio call devices voipcall and webcall, and H.264 between the network, the camera and the \
Phone app's call screen."
SECTION = "webos/services"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

# The engine is luneos-synergy-connectors' own code, in im/teams/calling/media/.
require recipes-luneos/synergy/luneos-synergy-connectors.inc

PV = "1.0+git"
PE = "1"

S = "${LSC_SRCDIR}"

DEPENDS = "gstreamer1.0 libnice glib-2.0"

inherit pkgconfig

EXTRA_OEMAKE = " \
    -C ${S}/im/teams/calling/media \
    PKG_CONFIG=pkg-config \
    libexecdir=${libexecdir} \
"

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake install DESTDIR=${D}
}

# The elements its pipeline builds at run time; nothing links them, so they are not found
# automatically:
#   nicesrc, nicesink              libnice
#   srtpenc, srtpdec               gstreamer1.0-plugins-bad-srtp (libsrtp with OpenSSL, for GCM)
#   rtpopuspay, rtpopusdepay       gstreamer1.0-plugins-good-rtp
#   rtpjitterbuffer, rtpptdemux    gstreamer1.0-plugins-good-rtpmanager
#   pulsesrc, pulsesink            gstreamer1.0-plugins-good-pulseaudio
#   opusenc, opusdec               gstreamer1.0-plugins-base-opus
#   audioconvert, audioresample    gstreamer1.0-plugins-base-audioconvert/-audioresample
#   capsfilter, queue, fakesink    gstreamer1.0 (coreelements)
# and for video:
#   rtph264pay, rtph264depay       gstreamer1.0-plugins-good-rtp
#   h264parse                      gstreamer1.0-plugins-bad-videoparsersbad
#   avdec_h264                     gstreamer1.0-libav
#   shmsink                        gstreamer1.0-plugins-bad-shm (the pictures the Phone app shows)
#   appsrc                         gstreamer1.0-plugins-base-app
#   videoconvert, videoscale       gstreamer1.0-plugins-base-videoconvertscale
#   droidcamsrc                    gst-droid, the camera and its H.264 encoder; halium only, so
#                                  elsewhere a video call only receives
RDEPENDS:${PN} = " \
    gstreamer1.0 \
    gstreamer1.0-libav \
    gstreamer1.0-plugins-bad-shm \
    gstreamer1.0-plugins-bad-srtp \
    gstreamer1.0-plugins-bad-videoparsersbad \
    gstreamer1.0-plugins-base-app \
    gstreamer1.0-plugins-base-audioconvert \
    gstreamer1.0-plugins-base-audioresample \
    gstreamer1.0-plugins-base-opus \
    gstreamer1.0-plugins-base-videoconvertscale \
    gstreamer1.0-plugins-good-pulseaudio \
    gstreamer1.0-plugins-good-rtp \
    gstreamer1.0-plugins-good-rtpmanager \
    libnice \
"
RDEPENDS:${PN}:append:halium = " gst-droid"
