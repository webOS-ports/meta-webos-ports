SUMMARY = "Exif, Iptc and XMP metadata manipulation library and tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=625f055f41728f84a8d7938acc35bdc2"

DEPENDS = "zlib expat"

SRC_URI = " \
  git://github.com/exiv2/exiv2.git;branch=0.27-maintenance \
"
S = "${WORKDIR}/git"
SRCREV = "62826516778b96da5ddb38f5ec1499dc76994a6f"

inherit cmake gettext
