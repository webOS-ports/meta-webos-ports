# Copyright (c) 2022 LG Electronics, Inc.

WEBOS_VERSION = "94.0.4606.128-13_15ba60c002b5823e44b07be6f6758f9910f0df01"

# 9.4.146.25-chromium94.3
SRCREV_v8 = "20f6074f6735d664fc398c1ca74aceb5d291d646"
SRCREV_main = "d593a3794841a4f840f22a660c1c52c2eb2afde5"

SRC_URI += "file://0001-grit-Remove-usage-of-U-mode-bit-for-opening-files-in.patch"
SRC_URI += "file://0001-Fix-compilation-with-lttng-ust-2.13.patch"
SRC_URI += "file://0001-zlib-Remove-unsetting-_FILE_OFFSET_BITS.patch"
# gcc-13 fixes:
SRC_URI += " \
    file://0001-Fix-build-with-gcc-13.patch;patchdir=src/v8 \
    file://0001-absl-strings-internal-str_format-extension.h-add-mis.patch \
    file://0001-Add-missing-include-to-ShaderVars.h.patch \
    file://0001-mainline-version-gcc-13-cannot-use-uintptr_t-via-inc.patch \
    file://0001-libgav1-Fix-build-with-gcc-13.patch \
    file://0001-perfetto-Fix-build-with-gcc-13.patch \
    file://0001-dawn-tint-Fix-build-with-gcc-13.patch \
    file://0001-VulkanMemoryAllocator-Added-missing-include-cstdio.patch \
    file://0001-webrtc-Fix-build-with-gcc-13.patch \
    file://0001-cc-trees-Fix-build-with-gcc-13.patch \
    file://0001-debug-Fix-build-with-gcc-13.patch \
    file://0001-feature_engagement-Fix-build-with-gcc-13.patch \
    file://0002-base-containers-Fix-build-with-gcc-13.patch \
    file://0003-gpu-config-Fix-build-with-gcc-13.patch \
    file://0004-password-manager-Fix-build-with-gcc-13.patch \
    file://0005-device-bluetooth-Fix-build-with-gcc-13.patch \
    file://0006-components-crash-Fix-build-with-gcc-13.patch \
    file://0007-device-base-Fix-build-with-gcc-13.patch \
    file://0008-net-cookies-Fix-build-with-gcc-13.patch \
    file://0009-net-base-Fix-build-with-gcc-13.patch \
    file://0010-quiche-Fix-build-with-gcc-13.patch \
    file://0011-ppapi-Fix-build-with-gcc-13.patch \
    file://0012-ui-Fix-build-with-gcc-13.patch \
    file://0013-autofill-Fix-build-with-gcc-13.patch \
    file://0014-payments-Fix-build-with-gcc-13.patch \
    file://0015-extensions-browser-api-Fix-build-with-gcc-13.patch \
    file://0016-third_party-blink-Fix-build-with-gcc-13.patch \
    file://0017-extensions-renderer-Fix-build-with-gcc-13.patch \
"

SRC_URI += " \
    file://0003-run_app_shell-fix-XDG_RUNTIME_DIR-for-LuneOS.patch \
    file://0004-Add-window.open-support.patch \
    file://0005-WebOS-Shell-Surface-add-client_size_changed-wayland-.patch \
    file://0007-DesktopNativeWidgetAura-donc-apply-DIP-for-SetBounds.patch \
    file://0008-palmGetResource-improve-legacy-compatibility.patch \
"
