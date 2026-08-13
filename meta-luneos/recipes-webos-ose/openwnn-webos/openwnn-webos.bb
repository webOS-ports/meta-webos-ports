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

do_install:append() {
    install -d  ${D}${libdir}/maliit/plugins
    install -m 755 ${S}/libWnnJpn.so ${D}${libdir}/maliit/plugins
}

# The shipped Makefile assigns CFLAGS absolutely (-fPIC -g -O2 -I...), so OE's
# CFLAGS -- and with them DEBUG_PREFIX_MAP -- never reach the compiler, and the
# debug info ends up recording absolute TMPDIR paths:
#   File /usr/lib/maliit/plugins/.debug/libWnnJpn.so in package openwnn-webos-dbg
#   contains reference to TMPDIR [buildpaths]
# Overriding CFLAGS on the make command line would drop the -I flags the
# Makefile bakes into it, so smuggle the flags in through TARGET_CC_ARCH, which
# lands in CXX itself. Same trick this recipe already uses for LDFLAGS.
TARGET_CC_ARCH += "${LDFLAGS} ${DEBUG_PREFIX_MAP}"
FILES:${PN} += "${libdir}/maliit/plugins/"
