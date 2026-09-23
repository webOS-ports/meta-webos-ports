FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

DEPENDS:remove = "libyuv"

PACKAGECONFIG = "gst"

LIBCAMERA_PIPELINES:aarch64 = "rkisp1,simple,uvcvideo,vimc"

# IPA modules are built per enabled pipeline. rkisp1's carries the AE/AWB
# loop, so without it the sensor streams but nothing drives exposure or gain.
EXTRA_OEMESON += "-Dipas=rkisp1,simple,vimc"

SRC_URI += "file://0001-ipa-rkisp1-enable-the-untuned-static-algorithms-for-PinePhonePro-sensors.patch"

SRC_URI += "file://0002-ipa-rkisp1-cproc-take-brightness-contrast-saturation-from-tuning-file.patch"

SRC_URI += "file://0003-gstreamer-do-not-mismatch-request-completions-across-a-restart.patch"

SRC_URI += "file://0004-pipeline-simple-support-the-Rockchip-VICAP-capture-block.patch"

SRC_URI += "file://0005-ipa-libipa-add-a-camera-sensor-helper-for-the-OV5648.patch"
SRC_URI += "file://0006-software_isp-align-the-debayer-output-stride-for-gpu-import.patch"
SRC_URI += "file://0007-ipa-simple-add-a-tuning-file-for-the-OV5648.patch"
SRC_URI += "file://0008-ipa-libipa-add-a-camera-sensor-helper-for-the-GC02M2.patch"
SRC_URI += "file://0009-ipa-simple-add-a-tuning-file-for-the-GC02M2.patch"

SRC_URI += "file://0010-ipa-rkisp1-support-the-rk3566-rk3568-V21-ISP.patch"
SRC_URI += "file://0011-ipa-rkisp1-awb-program-the-measurement-window-every-frame.patch"
