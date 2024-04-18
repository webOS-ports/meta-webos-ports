# Copyright (c) 2018-2023 LG Electronics, Inc.

EXTENDPRAUTO:append = "webos6"

# Remove rsvg because that's the only thing pulling librsvg -> cargo-native -> rust-native into our images
# and Tofee's builder takes 2h+ to build it
PACKAGECONFIG:remove = "rsvg"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI:append = " \
    file://0004-waylandsink-remove-unsupported-subcompositor.patch;striplevel=3 \
    file://0005-h264parse-resolution-changed-event-support.patch;striplevel=3 \
    file://0006-fix-Webex-meeting-Participant-video-screen-is-gray.patch \
"
