FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# The GStreamer-specific Qt SPI (QGStreamerVideoSource + QGStreamerInterface,
# QtMultimedia/spi/) is off by default (AUTODETECT false). The Camera app
# needs it on Halium devices to feed gst-droid's droidcamsrc into a
# QMediaCaptureSession via setNativeVideoSource.
EXTRA_OECMAKE += "-DFEATURE_gstreamer_qt_api=ON"

SRC_URI += "file://0001-gstreamer-stop-the-pipeline-at-end-of-media.patch"

SRC_URI += "file://0002-gstreamer-honour-the-GstMemory-offset-when-importing-dmabuf-planes.patch"

SRC_URI += "file://0003-gstreamer-report-the-camera-position-from-the-libcamera-device-properties.patch"

SRC_URI += "file://0004-gstreamer-imagecapture-write-an-encoded-image-not-the-raw-frame.patch"

SRC_URI += "file://0005-gstreamer-rank-camera-pixel-formats-instead-of-opening-in-greyscale.patch"
