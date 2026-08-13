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
# No go-cross line here. Its PN is go-cross-${TUNE_PKGARCH} (go-cross.inc), not
# ${TARGET_ARCH}, so a TARGET_ARCH-keyed pin does nothing on 32-bit ARM, where
# the two differ (arm vs armv7at2hf-neon). Where they are equal - aarch64, x86-64 -
# it merely duplicates the key oe-core already sets, and bitbake says so:
#   Variable key PREFERRED_VERSION_go-cross-${TUNE_PKGARCH} (${GOVERSION})
#   replaces original key PREFERRED_VERSION_go-cross-x86_64 (${GOVERSION})
# tcmode-default.inc has PREFERRED_VERSION_go-cross-${TUNE_PKGARCH} ?= "${GOVERSION}",
# which picks up the GOVERSION set above, so go-cross is pinned correctly without it.
# (go-crosssdk is the opposite case: its PN really is keyed on SDK_SYS, so that
# line below is needed - oe-core keys its own on SDK_ARCH and misses.)
PREFERRED_VERSION_go-crosssdk-${SDK_SYS}      = "${GOVERSION}"
PREFERRED_VERSION_go-cross-canadian-${TRANSLATED_TARGET_ARCH} = "${GOVERSION}"
