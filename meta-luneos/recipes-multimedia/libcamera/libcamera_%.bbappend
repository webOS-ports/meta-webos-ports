FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# libyuv is only used by src/android, the Android HAL adapter, which we do not
# build. The recipe for it arrived on meta-openembedded master alongside the
# 0.7.2 uprev and does not exist on wrynose, so keeping the dependency would
# make the recipe unbuildable here.
DEPENDS:remove = "libyuv"

PACKAGECONFIG = "gst"

# The pipeline handler list has to be set with the :aarch64 override, not as a
# plain assignment. The recipe declares
#
#     ARM_PIPELINES = "imx8-isi,mali-c55,simple,uvcvideo"
#     LIBCAMERA_PIPELINES:aarch64 ??= "${ARM_PIPELINES}"
#
# and an override-qualified default wins over an unsuffixed assignment, so a
# plain "LIBCAMERA_PIPELINES = ..." here is silently dropped on every 64-bit
# machine. Neither list contains rkisp1, which is the handler the Rockchip ISP
# needs: the PinePhone Pro (rk3399, imx258 + ov8858) and the PineTab2 (rk3566)
# both route their sensors through it. Without it libcamera starts but owns
# nothing:
#
#     WARN IPAManager ipa_manager.cpp:158 No IPA found in '/usr/lib/libcamera/ipa'
#     Available cameras:
#     <none>
#
# so anything built on libcamera has no device to open, and an application
# falls back to plain v4l2src. That cannot work here: rkisp1 is a
# media-controller device whose sensor, CSI receiver, ISP and both resizers
# have to be configured to agree before streaming, and v4l2src only ever opens
# /dev/videoN. The subdevs stay at their power-on defaults (SRGGB10/800x600
# against a sensor at SBGGR10/4208x3120) and the driver rejects the mismatch:
#
#     rkisp1 ff920000.isp1: start pipeline failed -32
LIBCAMERA_PIPELINES:aarch64 = "rkisp1,simple,uvcvideo,vimc"

# IPA modules are built per enabled pipeline. rkisp1's carries the AE/AWB
# loop, so without it the sensor streams but nothing drives exposure or gain.
EXTRA_OEMESON += "-Dipas=rkisp1,simple,vimc"

# The rkisp1 tuning files for the PinePhone Pro sensors only ever carried lens
# shading tables - no output gamma, so the ISP hands out near-linear data and
# the preview is dark and flat (a white wall renders as mid grey). Enable the
# static algorithms libcamera's own tuning generator always emits and that need
# no calibration data, and give the ov8858 helper its black level.
SRC_URI += "file://0001-ipa-rkisp1-enable-the-untuned-static-algorithms-for-PinePhonePro-sensors.patch"

# No rkisp1 tuning file upstream carries a colour correction matrix, so
# saturated colours reach the screen muted - a bright red renders as dark
# orange. Let the tuning file set the ColorProcessing defaults so saturation
# can be adjusted per sensor without a rebuild.
SRC_URI += "file://0002-ipa-rkisp1-cproc-take-brightness-contrast-saturation-from-tuning-file.patch"

# Tearing down a libcamerasrc with requests in flight - what happens when an
# application switches between the front and back sensors - crashed or hung the
# application four different ways. Not fixed in v0.7.2 either.
SRC_URI += "file://0003-gstreamer-do-not-mismatch-request-completions-across-a-restart.patch"
