SUMMARY = "Hybris client glue for the droidmedia Android-side media services"
DESCRIPTION = "The glibc side of Halium's droidmedia: a small static library \
(hybris.c) that loads the Android libdroidmedia.so through libhybris, plus \
the public headers and pkg-config file that gst-droid builds against. The \
Android side (libdroidmedia.so, minimediaservice, camera_service) ships in \
the device's Halium system image."
HOMEPAGE = "https://github.com/Halium/droidmedia"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://hybris.c;beginline=1;endline=17;md5=9a803e29be0ae72531f28ca03d3e7610"

# Only useful together with an Android container.
COMPATIBLE_MACHINE = "^halium$"
PACKAGE_ARCH = "${MACHINE_ARCH}"

# The C ABI between hybris.c and the in-container libdroidmedia.so carries no
# stability guarantee, so SRCREV should track the revision the device's Halium
# system image was built from (vendor/halium/droidmedia). In practice it has held
# still - see the symbol comparison below - but that is an observation about the
# images we ship today, not a promise, and it is worth re-checking when a GSI
# moves rather than assuming.
PV = "0.20260710+git"
SRCREV = "3a9d2f8c4b2e7e10c5208f505b895cb42a6b98dc"
DROIDMEDIA_BRANCH = "halium-16.0"

# One revision for every machine. halium-arm64 used to pin sailfishos/droidmedia
# at 0.20250807.0 instead, on the grounds that it shipped the Android 14 GSI - but
# that machine now sets PREFERRED_VERSION_android-headers-halium = "16.0%", so the
# premise had gone stale and the override was pairing it with the wrong upstream.
#
# Safe across every GSI we ship, checked by reading the exported symbols out of
# each image's /system/lib64/libdroidmedia.so rather than by assuming: 9.0, 11.0,
# 13.0 and 14.0 export byte-identical sets of 80 droid_media_* symbols, and 16.0 is
# a strict superset adding exactly one, droid_media_camera_set_torch_mode. Nothing
# is ever removed. That one addition is resolved with __try_resolve_sym rather than
# the HYBRIS_WRAPPER macros, so on an older GSI it resolves to NULL and returns
# false instead of aborting; every mandatory symbol exists everywhere.
DROIDMEDIA_FETCH = "git://github.com/Halium/droidmedia.git;branch=${DROIDMEDIA_BRANCH};protocol=https"

SRC_URI = "${DROIDMEDIA_FETCH} \
    file://0001-recorder-runtime-encoder-control-glue.patch \
    file://0001-meson-make-the-Qt5-flashlight-helper-optional.patch \
"

inherit meson pkgconfig

# The flashlight helper is Qt5 + libresourceqt5 (Sailfish-specific); LuneOS
# is Qt6 and exposes torch differently.
EXTRA_OEMESON = "-Dflashlight=false"

# Everything this installs is build-time material (static lib, headers,
# pkg-config file, hybris.c reference copy) - the runtime package is empty.
ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += "${datadir}/droidmedia"
