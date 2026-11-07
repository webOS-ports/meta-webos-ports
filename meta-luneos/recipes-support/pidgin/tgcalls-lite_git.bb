SUMMARY = "tgcalls-lite, a Telegram call engine for tdlib-purple"
DESCRIPTION = "tgcalls-lite speaks Telegram's tgcalls V2 call protocol without Google's WebRTC: \
the signalling messages and their encryption, ICE with libnice, DTLS-SRTP with OpenSSL and \
libsrtp2, Opus audio and H.264 over RTP. It is a static library that tdlib-purple links."
SECTION = "libs"

require recipes-luneos/synergy/luneos-synergy-connectors.inc

# im/telegram/plugin/tgcalls-lite is the repository's own code, GPL-3.0-or-later. Parts of it are
# derived from Telegram's tgcalls, which is LGPL-3.0; its NOTICE says which, and COPYING.LGPL3 is
# tgcalls' licence.
LICENSE = "GPL-3.0-or-later & LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464 \
                    file://im/telegram/plugin/tgcalls-lite/COPYING.LGPL3;md5=e6a600fd5e1d9cbde2d983680233ad02"

PE = "1"
PV = "0.1+git"

S = "${LSC_SRCDIR}"
TGCALLS_DIR = "${S}/im/telegram/plugin/tgcalls-lite"

DEPENDS = "glib-2.0 libnice libsrtp openssl json-glib libopus pulseaudio"

inherit pkgconfig

do_compile() {
    oe_runmake -C ${TGCALLS_DIR} libtgcalls-lite.a tests
}

do_install() {
    install -d ${D}${libdir} ${D}${includedir}/tgcalls-lite
    install -m 0644 ${TGCALLS_DIR}/libtgcalls-lite.a ${D}${libdir}/
    install -m 0644 ${TGCALLS_DIR}/*.h ${D}${includedir}/tgcalls-lite/
}

# Only a static library and headers: everything goes to -staticdev/-dev, nothing to ${PN}.
ALLOW_EMPTY:${PN} = "1"
