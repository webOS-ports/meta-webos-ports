# Copyright (c) 2014-2015 LG Electronics, Inc.
# Copyright (c) 2019 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "A BSON utility library"
DESCRIPTION = "libbson is a library providing useful routines related to building, parsing, and iterating BSON documents."
HOMEPAGE = "https://github.com/mongodb/libbson"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SECTION = "libs"

PR = "r1"
PV = "1.9.0+git${SRCPV}"
# corresponds to 1.9.0
SRCREV = "ffc8d983ecf6b46d5404f5cc20e756a85dfcbfd2"
SRC_URI = "git://github.com/mongodb/libbson.git"

S = "${WORKDIR}/git"

inherit cmake

do_configure:append() {
    find ${S}/src -type f -name "*.[c|h]" | xargs sed -i 's/\([^"|^\/]\)yajl_/\1bson_yajl_/g'
    find ${S}/src -type f -name "*.[c|h]" | xargs sed -i 's/^yajl_/bson_yajl_/g'
}
