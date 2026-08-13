# Copyright (c) 2012-2023 LG Electronics, Inc.

# Set default chromium version for webruntime and mksnapshot
PREFERRED_VERSION_webruntime = "120.%"
PREFERRED_VERSION_webruntime-clang = "${PREFERRED_VERSION_webruntime}"
PREFERRED_VERSION_mksnapshot-cross-${TARGET_ARCH} = "${PREFERRED_VERSION_webruntime}"

# Version 2 is available since nanbield:
# https://git.openembedded.org/meta-openembedded/commit/?id=cd26db61af27a9f822492b1d67b0e39e6828e7cb
# but eg25-manager used on pinepohone wasn't updated yet to support it:
# https://gitlab.com/mobian1/eg25-manager/-/issues/45
# meson 1.12.0 from meta-luneos-backports-6.0 rather than wrynose's 1.10.2.
# This is a forward-port, not a hold-back: oe-core wrynose regressed relative to
# what LuneOS was already building on scarthgap, so pin it explicitly or bitbake
# would pick 1.10.2 as the only other candidate.
PREFERRED_VERSION_meson = "1.12.0"
PREFERRED_VERSION_meson-native = "1.12.0"

PREFERRED_VERSION_libgpiod = "1.6.5"

# We need latest mesa from meta-luneos-backports-6.0 for all machines
PREFERRED_VERSION_mesa = "26.2.0%"


PREFERRED_VERSION_lttng-modules:tenderloin    = "2.13.12+git"

# The go 1.26.5 backport is gone: it existed because scarthgap shipped 1.22.12
# and purple-combined's go.mod needs >= 1.25.5 (whatsmeow, messagix). wrynose
# ships go 1.26.4 in oe-core, which satisfies that, so the whole
# meta-luneos/recipes-devtools/go tree and its GOVERSION pins are redundant.
# They were also broken here: every one of those recipes still had
# S = "${WORKDIR}/go", which wrynose rejects.
