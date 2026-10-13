SUMMARY = "Bridge Android (droidmedia) cameras to V4L2 via v4l2loopback"
DESCRIPTION = "FuriLabs' daemon that connects each Android camera through \
droidmedia and feeds its preview frames into a v4l2loopback node, so ordinary \
Linux V4L2 consumers (browsers/WebRTC, video-call apps) see the device cameras \
without going through gst-droid. LuneOS's own camera app keeps using gst-droid; \
this is an additive compatibility layer. Every camera droidmedia enumerates \
becomes its own /dev/video node named by facing."
HOMEPAGE = "https://github.com/FuriLabs/droidcam2v4l2"
SECTION = "multimedia"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=491030d225e002970a6e4eda1b06bb18"

# Needs the Android container (droidmedia + libhybris), so Halium only.
COMPATIBLE_MACHINE = "^halium$"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "git://github.com/FuriLabs/droidcam2v4l2.git;branch=forky;protocol=https \
           file://0001-wait-for-any-vendor-camera-HAL-service.patch \
           file://0002-dqevent-fallback-for-120-byte-pre-4.0-kernels.patch \
           file://0003-adapt-preview-format-rate-and-skip-af-on-fixed-focus.patch \
           file://droidcam2v4l2.service \
"
SRCREV = "6e7970d83efe290565dc6cd91fb2224d56998291"
PV = "0.0+git"

DEPENDS = "glib-2.0 droidmedia libhybris libyuv"

inherit pkgconfig systemd

# Upstream's Makefile hardcodes CC=gcc and only pkg-configs gio-2.0. Compile the
# single source directly so it cross-builds and resolves every dependency:
#  - droidmedia: a static libdroidmedia.a whose only non-droid_media_ undefined
#    symbols are dlopen/dlsym/dlclose, hence the -ldl its pkg-config carries;
#  - libandroid-properties (from libhybris): property_get();
#  - libyuv: I420Rotate - no libyuv.pc exists upstream, so -lyuv is named;
#  - pthread: the per-camera event-loop threads.
# The <droidmedia/...>, <hybris/...> and <libyuv.h> headers resolve from the
# sysroot's default include path.
do_compile() {
    ${CC} ${CFLAGS} \
        `pkg-config --cflags gio-2.0 libandroid-properties droidmedia` \
        -o droidcam2v4l2 ${S}/src/droidcam2v4l2.c \
        ${LDFLAGS} \
        `pkg-config --libs gio-2.0 libandroid-properties droidmedia` \
        -lyuv -lpthread
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${B}/droidcam2v4l2 ${D}${sbindir}/droidcam2v4l2

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/droidcam2v4l2.service ${D}${systemd_system_unitdir}/
}

SYSTEMD_SERVICE:${PN} = "droidcam2v4l2.service"
