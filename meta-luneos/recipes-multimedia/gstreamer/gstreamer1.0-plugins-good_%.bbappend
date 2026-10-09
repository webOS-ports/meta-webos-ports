FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
PACKAGECONFIG:append = " libv4l2"

SRC_URI += "file://0001-gst_v4l2_fill_lists-abort-if-type-is-invalid.patch"


# Halium devices capture and decode through gst-droid (droidcamsrc, droidvdec/venc); nothing there
# uses the v4l2 plugin - luneos-rtc-engine already drops its v4l2 recommends on halium. Built with
# -Dv4l2-probe=true it declares a dependency on the /dev/video* nodes, and on the HP TouchPad
# /dev/video20 (the legacy msm_v4l2 node) is recreated with a new mtime every boot, so the registry
# was invalidated and the plugin rescanned - probing the device - on every boot. Mainline-kernel
# machines keep it: there it is the camera and codec path.
PACKAGECONFIG:remove:halium = "v4l2 libv4l2"
