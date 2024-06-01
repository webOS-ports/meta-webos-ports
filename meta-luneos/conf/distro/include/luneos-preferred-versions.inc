# Copyright (c) 2012-2023 LG Electronics, Inc.

# Version 2 is available since nanbield:
# https://git.openembedded.org/meta-openembedded/commit/?id=cd26db61af27a9f822492b1d67b0e39e6828e7cb
# but eg25-manager used on pinepohone wasn't updated yet to support it:
# https://gitlab.com/mobian1/eg25-manager/-/issues/45
PREFERRED_VERSION_libgpiod = "1.6.4"

# Prefer gstreamer version 1.24.% from oe-core for webos
GSTREAMER_VERSION = "1.24.%"

PREFERRED_VERSION_gstreamer1.0              = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-bad  = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-base = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-cool = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-good = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-ugly = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-libav        = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-python       = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-rtsp-server  = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-vaapi        = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gst-devtools              = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gst-validate1.0           = "${GSTREAMER_VERSION}"
# gst-examples is not kept in sync with the rest of gst-* recipes
# PREFERRED_VERSION_gst-examples              = "${GSTREAMER_VERSION}"
