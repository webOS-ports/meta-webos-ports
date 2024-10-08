# Copyright (c) 2023-2024 LG Electronics, Inc.

WEBOS_REPO_NAME = "chromium108"
WEBRUNTIME_REPO_VERSION = "108"
WEBOS_VERSION = "108.0.5359.217-32_d9792956a6655070737c65bdc4fae1f69e224eb9"

# LICENSE
CHROMIUM_LICENSE_CHKSUM = "file://src/LICENSE;md5=c408a301e3407c3803499ce9290515d6"
# oss-pkg-info.yaml
CHROMIUM_OSS_PKG_INFO_CHKSUM = "file://oss-pkg-info.yaml;md5=f6cf1c62d76fef64d3ca1b3556fc1d9a"

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

SRC_URI += " \
    file://0001-Use-shlex-instead-of-pipes.patch \
    file://0001-Define-UChar-as-char16_t.patch \
"
