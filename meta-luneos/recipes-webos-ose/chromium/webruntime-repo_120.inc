# Copyright (c) 2024-2025 LG Electronics, Inc.

# LICENSE
CHROMIUM_LICENSE_CHKSUM = "file://src/LICENSE;md5=c408a301e3407c3803499ce9290515d6"
# oss-pkg-info.yaml
CHROMIUM_OSS_PKG_INFO_CHKSUM = "file://oss-pkg-info.yaml;md5=42bb8d47fa48b57155824ea9fa6595ad"

WEBOS_REPO_NAME = "chromium120"
WEBRUNTIME_REPO_VERSION = "120"
WEBOS_VERSION = "120.0.6099.269-16_83313e5b24304f67893b5121052f4cc1a00a7cf0"

# LuneOS specific patches
SRC_URI += " \
    file://0001-Add-window.open-support.patch \
    file://0002-WebOS-Shell-Surface-add-client_size_changed-wayland-.patch \
    file://0003-DesktopNativeWidgetAura-don-t-apply-DIP-for-SetBound.patch \
    file://0004-palmGetResource-improve-legacy-compatibility.patch \
    file://0005-palmGetResource-Fix-loading-of-local-resources.patch \
    file://0006-webossystem_injection.cc-fix-some-PalmSystem-functio.patch \
    file://0007-app_shell-override-default-OnWindowHostClose-to-clos.patch \
    file://0008-wayland_touch-apply-device_scale_factor-to-events.patch \
"