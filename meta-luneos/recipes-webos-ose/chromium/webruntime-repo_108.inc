# Copyright (c) 2023-2024 LG Electronics, Inc.

WEBOS_REPO_NAME = "chromium108"
WEBRUNTIME_REPO_VERSION = "108"
WEBOS_VERSION = "108.0.5359.217-16_0809da0cf5b0956ba3fcc91bad759716a7374c13"

# LICENSE
CHROMIUM_LICENSE_CHKSUM = "file://src/LICENSE;md5=c408a301e3407c3803499ce9290515d6"
# oss-pkg-info.yaml
CHROMIUM_OSS_PKG_INFO_CHKSUM = "file://oss-pkg-info.yaml;md5=f6cf1c62d76fef64d3ca1b3556fc1d9a"

# merged in upstream webruntime
SRC_URI += " \
    file://0001-Replace-imp.load_source-with-importlib-equivalent.patch \
    file://0002-grit-apply-pyupgrade.patch \
    file://0003-grit-remove-dependency-to-six.patch \
    file://0004-Remove-unused-python-import.patch \
    file://0005-More-removal-of-six.py.patch \
    file://0006-Update-vendored-copy-of-six-to-1.16.0.patch \
    file://0001-avoid-link-latomic-failure-on-CentOS-8-host.patch \
    file://0002-Revert-Disable-GPU-acceleration-on-VMware-on-Linux.patch \
"

# pending in upstream webruntime
SRC_URI += " \
    file://0002-Fix-build-with-gcc-13.patch \
    file://0003-Fix-build-with-dcheck_always_on-true-in-GN_ARGS.patch \
    file://0004-neva-Fix-build-with-dcheck_always_on-true-in-GN_ARGS.patch \
"

# LuneOS specific patches
SRC_URI += " \
    file://0001-Add-window.open-support.patch \
    file://0002-WebOS-Shell-Surface-add-client_size_changed-wayland-.patch \
    file://0003-DesktopNativeWidgetAura-don-t-apply-DIP-for-SetBound.patch \
    file://0004-palmGetResource-improve-legacy-compatibility.patch \
    file://0005-Update-app-shell.role.json.in-Add-trustLevel.patch \
    file://0006-palmGetResource-Fix-loading-of-local-resources.patch \
    file://0007-webossystem_injection.cc-fix-some-PalmSystem-functio.patch \
    file://0008-app_shell-override-default-OnWindowHostClose-to-clos.patch \
    file://0009-wayland_touch-apply-device_scale_factor-to-events.patch \
"
