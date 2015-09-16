# Copyright (c) 2014 LG Electronics, Inc.

SUMMARY = "LevelDB Template Library"
DESCRIPTION = "Template library to build a more complex storage schema with leveldb as a backend"
AUTHOR = "Nikolay Orliuk <virkony@gmail.com>"
HOMEPAGE = "https://github.com/ony/leveldb-tl"
SECTION = "libs"

# Different branches are using Apache-2.0 only, gcc-4.9 is a mix with LICENSE.md
# saying LGPL-2.1 and mksandwich.cpp having Apache-2.0 header, it looks like merge
# from gcc-4.7 branch didn't go well (LICENSE.md wasn't replaced with LICENSE.txt)
# LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"
LICENSE = "Apache-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=a0e609841f5eed88831111c74c9b90a0"

DEPENDS = "leveldb gtest"

SRC_URI = "git://github.com/ony/leveldb-tl.git;branch=gcc-4.9"

PV = "1.0.6+git${SRCPV}"
SRCREV = "515dc5da38aa2997ba4d44336bef2eeb44e5b0d2"

S = "${WORKDIR}/git"

inherit cmake
