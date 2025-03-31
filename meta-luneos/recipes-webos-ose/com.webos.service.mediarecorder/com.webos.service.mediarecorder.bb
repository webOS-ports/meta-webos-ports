# Copyright (c) 2023-2025 LG Electronics, Inc.

require com.webos.service.mediarecorder.inc

PR = "${INC_PR}.0"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.mediarecorder.service"

# disable test-apps PACKAGECONFIG, because it fails to build for qemux86 (doesn't fail for rpi)
PACKAGECONFIG += "build-media-recorder"

DEPENDS:append = " umediaserver media-resource-calculator"
