# Copyright (c) 2022-2023 LG Electronics, Inc.

WEBOS_VERSION = "94.0.4606.128-42_ca7be9386e921a320507174144ff758a75f886cc"

# 9.4.146.25-chromium94.7
SRCREV_v8 = "9e0278b87b7e59dd91364aaef32fd5fc31789bd4"

# http://gpro.lge.com/c/webosose/chromium94/+/363094 avcodec/x86/mathops: clip constants used with shift instructions within inline assembly
SRC_URI += "file://0001-avcodec-x86-mathops-clip-constants-used-with-shift-i.patch"

# http://gpro.lge.com/c/webosose/chromium94/+/373211 Disable usage of `__is_cpp17_contiguous_iterator` until libc++ rolls.
SRC_URI += "file://0001-Disable-usage-of-__is_cpp17_contiguous_iterator-unti-94.patch"

# LuneOS specific patches
SRC_URI += " \
    file://0002-run_app_shell-fix-XDG_RUNTIME_DIR-for-LuneOS.patch \
    file://0003-Add-window.open-support.patch \
    file://0004-WebOS-Shell-Surface-add-client_size_changed-wayland-.patch \
    file://0005-DesktopNativeWidgetAura-donc-apply-DIP-for-SetBounds.patch \
    file://0006-palmGetResource-improve-legacy-compatibility.patch \
    file://0007-Update-app-shell.role.json.in-Add-trustLevel.patch \
    file://0008-palmGetResource-Fix-loading-of-local-resources.patch \
    file://0009-webossystem_injection.cc-fix-some-PalmSystem-functions.patch \
    file://0010-app_shell-override-default-OnWindowHostClose-to-clos.patch \
"