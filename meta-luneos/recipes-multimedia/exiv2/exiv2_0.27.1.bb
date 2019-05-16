SUMMARY = "Exif, Iptc and XMP metadata manipulation library and tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=625f055f41728f84a8d7938acc35bdc2"

DEPENDS = "zlib expat"

SRC_URI = " \
  git://github.com/exiv2/exiv2.git;branch=0.27-maintenance \
"
S = "${WORKDIR}/git"
SRCREV = "16df09cc758e85d3d126701d2cdc8b447ded4743"

inherit cmake gettext
