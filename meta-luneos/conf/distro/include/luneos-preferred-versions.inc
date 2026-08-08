# Copyright (c) 2012-2023 LG Electronics, Inc.

# Set default chromium version for webruntime and mksnapshot
PREFERRED_VERSION_webruntime = "120.%"
PREFERRED_VERSION_webruntime-clang = "${PREFERRED_VERSION_webruntime}"
PREFERRED_VERSION_mksnapshot-cross-${TARGET_ARCH} = "${PREFERRED_VERSION_webruntime}"

# Version 2 is available since nanbield:
# https://git.openembedded.org/meta-openembedded/commit/?id=cd26db61af27a9f822492b1d67b0e39e6828e7cb
# but eg25-manager used on pinepohone wasn't updated yet to support it:
# https://gitlab.com/mobian1/eg25-manager/-/issues/45
PREFERRED_VERSION_libgpiod = "1.6.4"

# We need latest mesa from meta-mainline for all machines
PREFERRED_VERSION_mesa = "26.1.0%"

# Prefer gstreamer version 1.22.% from oe-core for webos
GSTREAMER_VERSION = "1.22.%"

PREFERRED_VERSION_gstreamer1.0              = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-bad  = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-base = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-cool = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-good = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-plugins-ugly = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-libav        = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-omx          = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-python       = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-rtsp-server  = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gstreamer1.0-vaapi        = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gst-devtools              = "${GSTREAMER_VERSION}"
PREFERRED_VERSION_gst-validate1.0           = "${GSTREAMER_VERSION}"
# gst-examples is not kept in sync with the rest of gst-* recipes
# PREFERRED_VERSION_gst-examples              = "${GSTREAMER_VERSION}"

PREFERRED_VERSION_lttng-modules:tenderloin    = "2.13.12+git"

# Go 1.26.5, backported from oe-core master into meta-luneos/recipes-devtools/go.
#
# scarthgap ships 1.22.12, and purple-combined's go.mod requires >= 1.25.5 -- the whatsmeow and
# messagix modules it depends on will not build on anything older ("go.mod requires go >= 1.25.5
# (running go 1.22.12)"). Every go recipe has to move together: go-cross, go-runtime and the
# native bootstrap all have to agree on a version or the cross build mixes toolchains.
GOVERSION = "1.26.5"
PREFERRED_VERSION_go                          = "${GOVERSION}"
PREFERRED_VERSION_go-native                   = "${GOVERSION}"
PREFERRED_VERSION_go-binary-native            = "${GOVERSION}"
PREFERRED_VERSION_go-runtime                  = "${GOVERSION}"
PREFERRED_VERSION_go-cross-${TARGET_ARCH}     = "${GOVERSION}"
PREFERRED_VERSION_go-crosssdk-${SDK_SYS}      = "${GOVERSION}"
PREFERRED_VERSION_go-cross-canadian-${TRANSLATED_TARGET_ARCH} = "${GOVERSION}"
