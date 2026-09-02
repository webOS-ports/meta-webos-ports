SUMMARY = "Real-time video media engine for calls on Halium devices"
DESCRIPTION = "Bridges the device cameras and hardware codecs to a call \
network stack: hardware H.264 access units stream out of (and decode back \
in through) SOCK_SEQPACKET unix sockets, one access unit per datagram - \
the interface shape the messaging connectors' call engines (for example \
meowcaller's Call.SendVideo/ReceiveVideo) exchange video in. TX runs \
droidcamsrc's recorder mode (encoder attached to the camera session in \
the Android layer, viewfinder-independent); RX decodes through droidvdec \
and renders on screen into a wl_webos_foreign imported window the call UI \
exports. Control planes: argv for standalone use, and the LS2 service \
org.webosports.rtcengine (start/stop/status) which acquires VENC/VDEC \
units from uMediaServer."
HOMEPAGE = "https://github.com/webOS-ports/luneos-rtc-engine"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base glib-2.0 luna-service2 libpbnjson umediaserver g-camera-pipeline qtbase qtdeclarative qtmultimedia wayland webos-wayland-extensions"
RDEPENDS:${PN} = "g-camera-pipeline"
# droidmedia capture/codec backend on Halium devices
RDEPENDS:${PN}:append:halium = " gst-droid"
# v4l2 capture and codec backend on mainline kernels, with software
# fallbacks; soft dependencies so absent packages (e.g. x264 without the
# commercial license flag) do not break the image
RRECOMMENDS:${PN} = " \
    gstreamer1.0-plugins-good-video4linux2 \
    gstreamer1.0-plugins-bad-v4l2codecs \
    gstreamer1.0-plugins-ugly-x264 \
    gstreamer1.0-libav \
"
RRECOMMENDS:${PN}:halium = ""

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "1.0.0+git"
SRCREV = "69c852895e1c1cb9161707bc491a06660bdf27af"
WEBOS_GIT_PARAM_BRANCH = "main"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit qt6-cmake pkgconfig

FILES:${PN} += "${datadir}/luna-service2 ${libdir}/qml"
