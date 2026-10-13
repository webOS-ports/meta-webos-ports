SUMMARY = "libyuv - YUV scaling, conversion and rotation library"
DESCRIPTION = "Google's libyuv: SIMD-optimised (NEON on arm) routines for YUV \
scaling, colour conversion and rotation. Pulled in here as the rotation \
backend for droidcam2v4l2 (I420Rotate)."
HOMEPAGE = "https://chromium.googlesource.com/libyuv/libyuv"
SECTION = "libs/multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=464282cfb405b005b9637f11103a7325"

# libyuv publishes no release tags - every consumer pins a commit. This is the
# upstream HEAD read on 2026-10-12 (git ls-remote), not a guess.
SRCREV = "eeb06a6451ba2e21f8ff272c7b42fc62b9faecc5"
PV = "0.0+git"

SRC_URI = "git://chromium.googlesource.com/libyuv/libyuv;protocol=https;branch=main"

inherit cmake

# The only consumer (droidcam2v4l2) uses I420Rotate, not the MJPEG helpers, so
# keep the dependency set minimal and the build deterministic by not letting
# CMake pick up a host/target libjpeg. libyuv compiles its MJPEG code out when
# JPEG is not found.
EXTRA_OECMAKE += "-DCMAKE_DISABLE_FIND_PACKAGE_JPEG=ON"

# libyuv's shared library carries no SONAME version, so the bare libyuv.so IS
# the runtime object, not a -dev symlink. Tell packaging it is the runtime solib
# and stop -dev from claiming it (a real .so in a -dev package tripped the
# dev-elf QA). The static libyuv.a and the headers still go to -staticdev/-dev.
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
