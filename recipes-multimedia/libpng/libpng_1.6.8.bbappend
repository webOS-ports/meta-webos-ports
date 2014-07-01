# Copyright (c) 2014 LG Electronics, Inc.

# Drop this .bbappend when upgrading to 1.6.10 or newer version of libpng
# CVE-2014-0333: http://sourceforge.net/projects/libpng/files/libpng16/patch-libpng-1.6.0-1.6.9-vu684412.diff/download
# CVE-2013-6954: https://lists.fedoraproject.org/pipermail/scm-commits/Week-of-Mon-20140127/1181090.html

PKGV .= "-0webos1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BP}:"

# temporary dropped because it breaks compilation in 1.6.3
# | pngset.c:418:9: error: 'num_palette' undeclared (first use in this function)
# | pngset.c:418:9: note: each undeclared identifier is reported only once for each function it appears in
# | pngset.c:418:28: error: 'palette' undeclared (first use in this function)
# SRC_URI += "file://CVE-2013-6954.patch"
SRC_URI += "file://patch-libpng-1.6.0-1.6.9-vu684412.diff"
