# Copyright (c) 2022 LG Electronics, Inc.

# 94.0.4606.128-28
SRCREV_main = "8dd0be73053420f8f7e65e2a768793d1e46e30c9"

# 9.4.146.25-chromium94.7
SRCREV_v8 = "9e0278b87b7e59dd91364aaef32fd5fc31789bd4"

SRC_URI += " \
    file://0003-run_app_shell-fix-XDG_RUNTIME_DIR-for-LuneOS.patch \
    file://0004-Add-window.open-support.patch \
    file://0005-WebOS-Shell-Surface-add-client_size_changed-wayland-.patch \
    file://0007-DesktopNativeWidgetAura-donc-apply-DIP-for-SetBounds.patch \
    file://0008-palmGetResource-improve-legacy-compatibility.patch \
"
