# Copyright (c) 2023 LG Electronics, Inc.

WEBOS_REPO_NAME = "chromium108"
WEBRUNTIME_REPO_VERSION = "108"
WEBOS_VERSION = "108.0.5359.217-10_f596a7307265fec379d890e4952e97e6f0e56298"

# LICENSE
CHROMIUM_LICENSE_CHKSUM = "file://src/LICENSE;md5=c408a301e3407c3803499ce9290515d6"
# oss-pkg-info.yaml
CHROMIUM_OSS_PKG_INFO_CHKSUM = "file://oss-pkg-info.yaml;md5=f6cf1c62d76fef64d3ca1b3556fc1d9a"

# LuneOS specific patches
SRC_URI += " \
    file://0001-run_app_shell-fix-XDG_RUNTIME_DIR-for-LuneOS.patch \
    file://0002-Add-window.open-support.patch \
    file://0003-WebOS-Shell-Surface-add-client_size_changed-wayland-.patch \
    file://0004-DesktopNativeWidgetAura-don-t-apply-DIP-for-SetBound.patch \
    file://0005-palmGetResource-improve-legacy-compatibility.patch \
    file://0006-Update-app-shell.role.json.in-Add-trustLevel.patch \
    file://0007-palmGetResource-Fix-loading-of-local-resources.patch \
    file://0008-webossystem_injection.cc-fix-some-PalmSystem-functio.patch \
    file://0009-app_shell-override-default-OnWindowHostClose-to-clos.patch \
"
