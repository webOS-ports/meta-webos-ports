FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Set-specific-media.role-for-pulsesink-probe.patch \
"

# Disable v4l2 support as tenderloin kernel is crashing on startup with it being enabled
EXTRA_OECONF += "--disable-gst_v4l2"
