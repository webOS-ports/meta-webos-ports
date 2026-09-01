# Copyright (c) 2018-2023 LG Electronics, Inc.

EXTENDPRAUTO:append = "webos7"

# Remove rsvg because that's the only thing pulling librsvg -> cargo-native -> rust-native into our images
# and Tofee's builder takes 2h+ to build it
PACKAGECONFIG:remove = "rsvg"
PACKAGECONFIG:append = " v4l2codecs"

# The zbar element decodes barcodes out of a video stream, which is how the
# eSIM settings page reads an operator's QR activation code. Nothing else on
# the image can decode a QR - libqrencode only writes them.
PACKAGECONFIG:append = " zbar"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI:append = " \
    file://0004-waylandsink-make-wl_subcompositor-optional.patch;striplevel=3 \
    file://0005-h264parse-resolution-changed-event-support.patch;striplevel=3 \
    file://0006-fix-Webex-meeting-Participant-video-screen-is-gray.patch;striplevel=3 \
"
