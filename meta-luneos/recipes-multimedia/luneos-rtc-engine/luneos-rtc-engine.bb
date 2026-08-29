SUMMARY = "Real-time video media engine for calls on Halium devices"
DESCRIPTION = "Bridges the device cameras and hardware codecs to a call \
network stack: hardware H.264 access units stream out of (and decode back \
in through) SOCK_SEQPACKET unix sockets, one access unit per datagram - \
the interface shape the messaging connectors' call engines (for example \
meowcaller's Call.SendVideo/ReceiveVideo) exchange video in. TX runs \
droidcamsrc's recorder mode (encoder attached to the camera session in \
the Android layer, viewfinder-independent); RX decodes through droidvdec. \
Validated as a full loopback at 30fps with one frame of latency. \
Staged here with in-recipe sources; graduates to its own repository \
together with the LS2/uMS pipeline wrapper."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "gstreamer1.0 glib-2.0"
RDEPENDS:${PN} = "gst-droid"

COMPATIBLE_MACHINE = "^halium$"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    file://rtc_engine.cpp \
    file://CMakeLists.txt \
"

S = "${UNPACKDIR}"

inherit cmake pkgconfig

do_configure:prepend() {
    mkdir -p ${S}/src
    cp ${UNPACKDIR}/rtc_engine.cpp ${S}/src/ 2>/dev/null || true
}
