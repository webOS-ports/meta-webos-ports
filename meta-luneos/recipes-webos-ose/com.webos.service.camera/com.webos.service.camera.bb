# Copyright (c) 2019-2025 LG Electronics, Inc.

SUMMARY = "Camera service framework to control camera devices"
AUTHOR = "Sungho Lee <shl.lee@lge.com>"
SECTION = "webos/services"

require com.webos.service.camera.inc

SRC_URI += " \
    file://0001-plugin-rename-basename-function-to-avoid-conflict-wi.patch \
    file://0003-com.webos.service.camera2-allow-outbound-to-faceunloc.patch \
"

# Halium: droid HAL + notifier plugins reach the Android cameras through
# gst-droid (runtime dependency; the plugins no-op without it).
SRC_URI:append:halium = " \
    file://0001-Add-droid-camera-HAL-and-notifier-plugins.patch \
    file://0004-notifier-droid-probe-droidcamsrc-off-the-service-main.patch \
    file://0005-hal-droid-report-why-the-droid-pipeline-failed-to-sta.patch \
    file://0006-Process-do-not-leak-the-parent-s-descriptors-into-the.patch \
    file://0007-hal-droid-bound-the-pipeline-teardown-so-a-failure-is.patch \
    file://0008-hal-droid-retry-the-pipeline-start-instead-of-giving-.patch \
    file://0009-hal-droid-build-the-pipeline-off-the-luna-service2-ha.patch \
"
RRECOMMENDS:${PN}:append:halium = " gst-droid"
PR = "${INC_PR}.3"

DEPENDS = "glib-2.0 luna-service2 json-c alsa-lib pmloglib udev nlohmann-json camera-utils gstreamer1.0"

# depends on edgeai-vision
PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'webos-aiframework', d)}\
"

PACKAGECONFIG[webos-aiframework] = "-DWITH_AIFRAMEWORK=ON,-DWITH_AIFRAMEWORK=OFF,edgeai-vision"
inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.camera.service camera-registry.service"
WEBOS_SYSTEMD_SCRIPT = "camera-registry.sh"

# To scan the plugins used by the camera service in /usr/lib/camera.
FILES:${PN}-dev += "${libdir}/camera/lib*${SOLIBSDEV}"
FILES:${PN} += "${libdir}/camera/lib*${SOLIBS}"

inherit useradd
USERADD_PACKAGES = "${PN}"

USERADD_PARAM:${PN} = " \
    -u 1006 -d /var -s /usr/sbin/nologin -G video -U camera; \
"

GROUPMEMS_PARAM:${PN} = " \
    -a camera -g video; \
"
