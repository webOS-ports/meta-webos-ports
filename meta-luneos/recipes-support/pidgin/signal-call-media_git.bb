SUMMARY = "Media engine for 1:1 Signal voice calls"
DESCRIPTION = "signal-call-media carries the audio of a Signal call: ICE with libnice, SRTP with \
AEAD_AES_256_GCM keyed from the call's X25519 DH, and Opus over RTP, played to and captured from \
the PulseAudio call sink and source. purple-presage starts one for each call and drives it over \
stdin/stdout."
SECTION = "webos/services"

require recipes-luneos/synergy/luneos-synergy-connectors.inc

# im/signal/calling/media is the repository's own code.
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

PV = "1.0+git"

S = "${LSC_SRCDIR}"
MEDIA_DIR = "${S}/im/signal/calling/media"

# libsrtp needs its OpenSSL backend for AEAD_AES_256_GCM (libsrtp_%.bbappend); openssl is also
# libcrypto for the X25519 and HKDF in srtp_kdf.c. speexdsp is the echo canceller.
DEPENDS = "glib-2.0 gstreamer1.0 gstreamer1.0-plugins-base libnice libsrtp openssl speexdsp"

inherit pkgconfig

do_compile() {
    oe_runmake -C ${MEDIA_DIR} signal_media
}

do_install() {
    oe_runmake -C ${MEDIA_DIR} install DESTDIR=${D} libexecdir=${libexecdir}
}

# The GStreamer elements the pipeline builds by name, which nothing links against:
#   nicesrc, nicesink                         libnice (its gstreamer PACKAGECONFIG)
#   srtpenc, srtpdec                          gstreamer1.0-plugins-bad-srtp
#   opusenc, opusdec                          gstreamer1.0-plugins-base-opus
#   audioconvert, audioresample, appsrc       gstreamer1.0-plugins-base-{audioconvert,audioresample,app}
#   capsfilter, funnel, queue, fakesink       gstreamer1.0 (coreelements)
#   rtpopuspay, rtpopusdepay                  gstreamer1.0-plugins-good-rtp
#   rtpjitterbuffer                           gstreamer1.0-plugins-good-rtpmanager
#   pulsesink, pulsesrc                       gstreamer1.0-plugins-good-pulseaudio
#   audiobuffersplit                          gstreamer1.0-plugins-bad-audiobuffersplit
RDEPENDS:${PN} += " \
    libnice \
    gstreamer1.0-plugins-bad-srtp \
    gstreamer1.0-plugins-bad-audiobuffersplit \
    gstreamer1.0-plugins-base-opus \
    gstreamer1.0-plugins-base-audioconvert \
    gstreamer1.0-plugins-base-audioresample \
    gstreamer1.0-plugins-base-app \
    gstreamer1.0 \
    gstreamer1.0-plugins-good-rtp \
    gstreamer1.0-plugins-good-rtpmanager \
    gstreamer1.0-plugins-good-pulseaudio \
"
