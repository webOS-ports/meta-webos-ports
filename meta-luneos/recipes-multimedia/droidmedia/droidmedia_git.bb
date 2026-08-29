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

# The C ABI between hybris.c and the in-container libdroidmedia.so is not
# stable across revisions: keep SRCREV identical to the revision the device's
# Halium system image was built from (vendor/halium/droidmedia).
PV = "0.20260710+git"
SRCREV = "3a9d2f8c4b2e7e10c5208f505b895cb42a6b98dc"
DROIDMEDIA_BRANCH = "halium-16.0"

# halium-arm64 images currently ship the Android 14 GSI, whose droidmedia is
# sailfishos/droidmedia at tag 0.20250807.0 - the halium-14.0 manifest tracks
# the sailfishos repo, and the Halium fork has no halium-14.0 branch, hence
# nobranch=1. That revision has no flashlight helper (and no
# meson_options.txt), so neither the patch below nor -Dflashlight apply.
PV:halium-arm64 = "0.20250807+git"
SRCREV:halium-arm64 = "5c859c50de3ff1007fb15e9b4a955a63be956804"
DROIDMEDIA_FETCH = "git://github.com/Halium/droidmedia.git;branch=${DROIDMEDIA_BRANCH};protocol=https"
DROIDMEDIA_FETCH:halium-arm64 = "git://github.com/sailfishos/droidmedia.git;nobranch=1;protocol=https"

SRC_URI = "${DROIDMEDIA_FETCH} \
    file://0001-recorder-runtime-encoder-control-glue.patch \
    file://0001-meson-make-the-Qt5-flashlight-helper-optional.patch \
"
SRC_URI:remove:halium-arm64 = "file://0001-meson-make-the-Qt5-flashlight-helper-optional.patch"

inherit meson pkgconfig

# The flashlight helper is Qt5 + libresourceqt5 (Sailfish-specific); LuneOS
# is Qt6 and exposes torch differently.
EXTRA_OEMESON = "-Dflashlight=false"
EXTRA_OEMESON:halium-arm64 = ""

# Everything this installs is build-time material (static lib, headers,
# pkg-config file, hybris.c reference copy) - the runtime package is empty.
ALLOW_EMPTY:${PN} = "1"
FILES:${PN}-dev += "${datadir}/droidmedia"
