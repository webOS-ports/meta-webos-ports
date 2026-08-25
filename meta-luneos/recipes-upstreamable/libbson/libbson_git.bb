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

inherit cmake

do_configure:append() {
    find ${S}/src -type f -name "*.[c|h]" | xargs sed -i 's/\([^"|^\/]\)yajl_/\1bson_yajl_/g'
    find ${S}/src -type f -name "*.[c|h]" | xargs sed -i 's/^yajl_/bson_yajl_/g'
}

# The static variant records every implicit library CMake resolved as a full
# path - ${STAGING_DIR_HOST}${libdir}/librt.so and friends - rather than as a
# -l flag, so both the pkg-config and cmake files hand the build tree to anyone
# who links libbson statically. Rewrite them back to -l instead of demoting the
# QA check, which previously hid it for the whole recipe.
do_install:append() {
    for f in ${D}${libdir}/pkgconfig/libbson-static-1.0.pc \
             ${D}${libdir}/cmake/libbson-static-1.0/libbson-static-1.0-config.cmake; do
        [ -f "$f" ] || continue
        sed -i -e 's|${STAGING_DIR_HOST}${libdir}/lib\([a-zA-Z0-9_+-]*\)\.so|-l\1|g' \
               -e 's|${STAGING_DIR_HOST}||g' "$f"
    done
}
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
