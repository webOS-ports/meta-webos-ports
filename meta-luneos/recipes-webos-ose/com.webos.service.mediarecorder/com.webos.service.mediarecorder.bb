# Copyright (c) 2023-2025 LG Electronics, Inc.

require com.webos.service.mediarecorder.inc

PR = "${INC_PR}.0"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "com.webos.service.mediarecorder.service"

# disable test-apps and build-media-recorder PACKAGECONFIG, because it fails to build for qemux86 (doesn't fail for rpi)
# PACKAGECONFIG += "build-media-recorder test-apps"

DEPENDS:append = " umediaserver media-resource-calculator"

# GCC 15 fires -Wfree-nonheap-object inside libstdc++'s new_allocator.h when it
# inlines the destruction of the local std::vector<audio_support_list_t> that
# isSupportedAudioFormat() builds from an initializer list:
#   new_allocator.h:172:66: error: 'void operator delete(void*, unsigned long)'
#   called on pointer with nonzero offset 104 [-Werror=free-nonheap-object]
# The offset is an interior member of the struct; the vector is an ordinary
# local destroyed at scope exit, so there is nothing to fix in the code. This
# warning is well known for losing track through inlining, so demote it here
# rather than editing correct source to appease it.
CXXFLAGS:append = " -Wno-error=free-nonheap-object"
