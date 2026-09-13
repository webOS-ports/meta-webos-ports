FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# The GStreamer-specific Qt SPI (QGStreamerVideoSource + QGStreamerInterface,
# QtMultimedia/spi/) is off by default (AUTODETECT false). The Camera app
# needs it on Halium devices to feed gst-droid's droidcamsrc into a
# QMediaCaptureSession via setNativeVideoSource.
EXTRA_OECMAKE += "-DFEATURE_gstreamer_qt_api=ON"

# QGstreamerMediaPlayer reports StoppedState at end-of-media without ever taking
# the GstPlay pipeline out of GST_STATE_PLAYING, so a finished player keeps its
# audio sink open: pulsesink corks only on a PLAYING->PAUSED transition, the
# stream stays uncorked, module-suspend-on-idle can never suspend the output
# sink, and PulseAudio renders silence for the lifetime of the player. On sargo
# that cost 10.1% of a CPU core continuously for a single boot chime. The patch
# header carries the full measurement.
SRC_URI += "file://0001-gstreamer-stop-the-pipeline-at-end-of-media.patch"
