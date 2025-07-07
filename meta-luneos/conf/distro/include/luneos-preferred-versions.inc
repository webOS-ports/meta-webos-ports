# Copyright (c) 2012-2023 LG Electronics, Inc.

# Set default chromium version for webruntime and mksnapshot
PREFERRED_VERSION_webruntime = "120.%"
PREFERRED_VERSION_webruntime-clang = "${PREFERRED_VERSION_webruntime}"
PREFERRED_VERSION_mksnapshot-cross-${TARGET_ARCH} = "${PREFERRED_VERSION_webruntime}"

# Version 2 is available since nanbield:
# https://git.openembedded.org/meta-openembedded/commit/?id=cd26db61af27a9f822492b1d67b0e39e6828e7cb
# but eg25-manager used on pinepohone wasn't updated yet to support it:
# https://gitlab.com/mobian1/eg25-manager/-/issues/45
PREFERRED_VERSION_libgpiod = "1.6.5"

PREFERRED_VERSION_lttng-modules:tenderloin    = "2.13.12+git"
