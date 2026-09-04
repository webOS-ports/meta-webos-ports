# Copyright (c) 2019-2025 LG Electronics, Inc.

SUMMARY = "A Japanese IME library (input method editor for typing Japanese)"
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=34e549453b3e73c1d635e93b4a01b96b \
    file://oss-pkg-info.yaml;md5=9e866a0c61ba2b36863c702e9a4c9163 \
"

inherit pkgconfig
inherit webos_enhanced_submissions
inherit webos_public_repo

WEBOS_VERSION = "1.0.0-8_4c800b266bf5d217a55ab1bc509b01fd96cb66cc"
PR = "r0"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"

# The upstream Makefile assigns CFLAGS/LDFLAGS unconditionally, so the flags
# bitbake exports in the environment - including DEBUG_PREFIX_MAP - are thrown
# away and the debug info ends up carrying build paths. Override CFLAGS on the
# make command line, where it wins over the makefile assignment, keeping the
# include paths and -fPIC the makefile needs for the shared library.
EXTRA_OEMAKE = "CFLAGS='-fPIC ${CXXFLAGS} -I./libs/libwnnDictionary/include -I./libs/libwnnDictionary -I./src/include'"

do_install:append() {
    install -d  ${D}${libdir}/maliit/plugins
    install -m 755 ${S}/libWnnJpn.so ${D}${libdir}/maliit/plugins
}

TARGET_CC_ARCH += "${LDFLAGS}"
FILES:${PN} += "${libdir}/maliit/plugins/"
