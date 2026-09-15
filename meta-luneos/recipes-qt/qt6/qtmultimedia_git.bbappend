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

# Qt's dmabuf import assumes that a buffer carrying one GstMemory per plane has
# every plane at offset 0 of its own dma-buf, and drops the GstMemory offsets.
# libcamerasrc shares a single fd across all planes of a single-planar format
# and distinguishes them only by that offset, so all three planes of an I420
# frame were imported over the luma plane - a green preview on every camera.
SRC_URI += "file://0002-gstreamer-honour-the-GstMemory-offset-when-importing-dmabuf-planes.patch"

# findVideoInputs() never sets QCameraDevicePrivate::position, so every camera
# reports UnspecifiedPosition and an app cannot tell front from back. libcamera
# publishes it as api.libcamera.Location on the GstDevice; map it across.
SRC_URI += "file://0003-gstreamer-report-the-camera-position-from-the-libcamera-device-properties.patch"

# The image capture probe sits upstream of the bin's jpegenc, and the encoder
# branch ends in a fakesink, so every captured file was an uncompressed frame
# with a .jpg name (1382400 bytes at 1280x720 I420). Save the converted QImage.
SRC_URI += "file://0004-gstreamer-imagecapture-write-an-encoded-image-not-the-raw-frame.patch"

# findBestCameraFormat()'s ranking tuple is degenerate for libcamera devices, so
# it returns the first enumerated format - greyscale. Applications then have to
# re-assign cameraFormat, which reconfigures the camera a second time; doing
# that during a sensor switch reconfigures three times and hangs the pipeline.
SRC_URI += "file://0005-gstreamer-rank-camera-pixel-formats-instead-of-opening-in-greyscale.patch"
