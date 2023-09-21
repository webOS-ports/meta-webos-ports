# Copyright (c) 2022-2023 LG Electronics, Inc.

#WEBOS_VERSION = "94.0.4606.128-42_ca7be9386e921a320507174144ff758a75f886cc"
SRCREV_main = "ca7be9386e921a320507174144ff758a75f886cc"

# 9.4.146.25-chromium94.7
SRCREV_v8 = "9e0278b87b7e59dd91364aaef32fd5fc31789bd4"

# http://gpro.lge.com/c/webosose/chromium94/+/363094 avcodec/x86/mathops: clip constants used with shift instructions within inline assembly
SRC_URI += " \
    file://0001-avcodec-x86-mathops-clip-constants-used-with-shift-i.patch \
    file://0003-run_app_shell-fix-XDG_RUNTIME_DIR-for-LuneOS.patch \
    file://0004-Add-window.open-support.patch \
    file://0005-WebOS-Shell-Surface-add-client_size_changed-wayland-.patch \
    file://0007-DesktopNativeWidgetAura-donc-apply-DIP-for-SetBounds.patch \
    file://0008-palmGetResource-improve-legacy-compatibility.patch \
"
