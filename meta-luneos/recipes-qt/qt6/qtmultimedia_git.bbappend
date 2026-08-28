# The GStreamer-specific Qt SPI (QGStreamerVideoSource + QGStreamerInterface,
# QtMultimedia/spi/) is off by default (AUTODETECT false). The Camera app
# needs it on Halium devices to feed gst-droid's droidcamsrc into a
# QMediaCaptureSession via setNativeVideoSource.
EXTRA_OECMAKE += "-DFEATURE_gstreamer_qt_api=ON"
