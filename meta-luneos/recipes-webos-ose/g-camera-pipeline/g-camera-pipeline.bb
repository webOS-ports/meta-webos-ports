# Copyright (c) 2019-2024 LG Electronics, Inc.

SUMMARY = "g-camera-pipeline is a player which uses GStreamer"
AUTHOR = "Kalaimani K <kalaimani.k@lge.com>"
SECTION = "webos/media"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

inherit webos_cmake
inherit webos_system_bus
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_machine_dep
inherit pkgconfig

PR = "r14"

DEPENDS = "boost gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad pkgconfig umediaserver media-resource-calculator com.webos.service.camera webos-wayland-extensions"
DEPENDS:append:rpi = " userland"

#In LuneOS we want this for all machines
#COMPATIBLE_MACHINE = "^qemux86$|^qemux86-64$|^raspberrypi3$|^raspberrypi3-64$|^raspberrypi4$|^raspberrypi4-64$"

WEBOS_VERSION = "1.0.0-gav.44_efe4ae4576379afe03abbf71fe430c8b360bc838"

WEBOS_GIT_PARAM_BRANCH = "@gav"
SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-g-camera-pipeline-Add-generic-config.patch \
"

S = "${WORKDIR}/git"

FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/*.so"
FILES:${PN} += "${libdir}/gstreamer-1.0/*.so"

# g-camera-pipeline/1.0.0-gav.44/git/camsrc/gstcamsrc.c:168:36: error: assignment to 'GstStateChangeReturn (*)(GstElement *, GstStateChange)' {aka 'GstStateChangeReturn (*)(struct _GstElement *, GstStateChange)'} from incompatible pointer type 'GstStateChangeReturn (*)(GstPushSrc *, GstStateChange)' {aka 'GstStateChangeReturn (*)(struct _GstPushSrc *, GstStateChange)'} [-Wincompatible-pointer-types]
# g-camera-pipeline/1.0.0-gav.44/git/camsrc/gstcamsrc.c:389:73: error: passing argument 2 of 'camera_hal_if_get_buffer_fd' from incompatible pointer type [-Wincompatible-pointer-types]
# g-camera-pipeline/1.0.0-gav.44/git/camsrc/gstcamsrc.c:515:30: error: implicit declaration of function 'camera_hal_if_destroy_dmafd'; did you mean 'camera_hal_if_destroy_buffer'? [-Wimplicit-function-declaration]
# g-camera-pipeline/1.0.0-gav.44/git/camsrc/gstcamsrc.c:523:59: error: passing argument 1 of '((GstElementClass *)parent_class)->change_state' from incompatible pointer type [-Wincompatible-pointer-types]
CFLAGS += "-Wno-error=incompatible-pointer-types -Wno-error=implicit-function-declaration"
