# Copyright (c) 2014-2015 LG Electronics, Inc.
# Copyright (c) 2019 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "A BSON utility library"
DESCRIPTION = "libbson is a library providing useful routines related to building, parsing, and iterating BSON documents."
HOMEPAGE = "https://github.com/mongodb/libbson"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

SECTION = "libs"

PR = "r1"
PV = "1.9.0+git"
# corresponds to 1.9.0
SRCREV = "ffc8d983ecf6b46d5404f5cc20e756a85dfcbfd2"
SRC_URI = "git://github.com/mongodb/libbson.git;branch=master;protocol=https"

S = "${WORKDIR}/git"

inherit cmake

do_configure:append() {
    find ${S}/src -type f -name "*.[c|h]" | xargs sed -i 's/\([^"|^\/]\)yajl_/\1bson_yajl_/g'
    find ${S}/src -type f -name "*.[c|h]" | xargs sed -i 's/^yajl_/bson_yajl_/g'
}

# ERROR: libbson-1.9.0+git-r1 do_package_qa: QA Issue: File /usr/lib/cmake/libbson-static-1.0/libbson-static-1.0-config.cmake in package libbson-dev contains reference to TMPDIR
# File /usr/lib/pkgconfig/libbson-static-1.0.pc in package libbson-dev contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
