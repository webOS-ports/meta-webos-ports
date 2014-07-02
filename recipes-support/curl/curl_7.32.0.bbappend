# Copyright (c) 2012-2014 LG Electronics, Inc.

PKGV .= "-0webos1"
EXTENDPRAUTO_append = "webos2"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BP}:"

# libcurl 7.21.4 through 7.33.0
SRC_URI += "file://cve-2013-6422.patch"
# libcurl 7.10.6 through 7.34.0
SRC_URI += "file://CVE-2014-0015.patch"
# libcurl 7.1 before 7.36.0, CVE-2014-0139
SRC_URI += "file://libcurl-reject-cert-ip-wildcards.patch"

DEPENDS_append_class-target = " c-ares"

EXTRA_OECONF_append_class-target = " --enable-ares"
