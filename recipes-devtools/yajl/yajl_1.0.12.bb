DESCRIPTION = "Yet Another JSON Library - A Portable JSON parsing and serialization library in ANSI C"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=da2e9aa80962d54e7c726f232a2bd1e8"

inherit cmake

# corresponds to tag 1.0.12
# SRCREV = "17b1790fb9c8abbb3c0f7e083864a6a014191d56"
# but because it's not included in any branch we need to use parent of that
SRCREV = "6a8906d409dfe6dd3f1e01ed066aa162b1729cec"
SRC_URI = "git://github.com/lloyd/${PN};branch=1.x"

S = "${WORKDIR}/git"

