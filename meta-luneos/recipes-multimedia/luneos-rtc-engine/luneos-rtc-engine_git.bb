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

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base glib-2.0 luna-service2 libpbnjson umediaserver g-camera-pipeline qtbase qtdeclarative wayland webos-wayland-extensions"
RDEPENDS:${PN} = "gst-droid g-camera-pipeline"

COMPATIBLE_MACHINE = "^halium$"
PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "1.0.0+git"
SRCREV = "db8107e643db3c954ad4414e7be719fc02c10b72"
SRC_URI = "git://github.com/webOS-ports/luneos-rtc-engine.git;branch=main;protocol=https"


inherit qt6-cmake pkgconfig

FILES:${PN} += "${datadir}/luna-service2 ${libdir}/qml"
