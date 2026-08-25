# Copyright (c) 2012-2023 LG Electronics, Inc.

# Set default chromium version for webruntime and mksnapshot.
# 151 is the default on every machine as of 2026-08-20. Set this back to
# "120.%" to return the whole distro to Chromium 120; both recipe sets are
# still present.
PREFERRED_VERSION_webruntime = "151.%"
PREFERRED_VERSION_webruntime-clang = "${PREFERRED_VERSION_webruntime}"
PREFERRED_VERSION_mksnapshot-cross-${TARGET_ARCH} = "${PREFERRED_VERSION_webruntime}"

# meson 1.12.0 from meta-luneos-backports-6.0 rather than wrynose's 1.10.2.
# This is a forward-port, not a hold-back: oe-core wrynose regressed relative to
# what LuneOS was already building on scarthgap, so pin it explicitly or bitbake
# would pick 1.10.2 as the only other candidate.
PREFERRED_VERSION_meson = "1.12.0"
PREFERRED_VERSION_meson-native = "1.12.0"

# No PREFERRED_VERSION_libgpiod: eg25-manager, the only recipe in the tree that
# uses libgpiod, now requires >= 2.0 ("meson.build:62: Invalid version, need
# libgpiod ['>= 2.0'] found '1.6.5'"), so let bitbake take the newest meta-oe
# provides. There used to be a pin here holding libgpiod at 1.x for the opposite
# reason (https://gitlab.com/mobian1/eg25-manager/-/issues/45), but it named
# 1.6.4, a version meta-oe stopped carrying, so it silently resolved to the
# newest anyway and eg25-manager has been building against libgpiod 2.x for
# some time. Repairing it to a version that does exist is what broke the build.

# We need latest mesa from meta-luneos-backports-6.0 for all machines
PREFERRED_VERSION_mesa = "26.2.0%"

PREFERRED_VERSION_lttng-modules:tenderloin    = "2.13.12+git"
