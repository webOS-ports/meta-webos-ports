# Copyright (c) 2012-2013 LG Electronics, Inc.

PACKAGES = "fuser killall pstree \
            ${PN}-dbg ${PN}-staticdev ${PN} \
            ${PN}-dev ${PN}-locale psmisc-extras \
"

do_configure_prepend() {
    sed -i '/^SUBDIRS/s/=.*$/= src/' Makefile.am
    sed -i '/^oldfuser_/s/^/#/' src/Makefile.am
    sed -i '/^bin_PROGRAMS/s/ oldfuser//' src/Makefile.am
}
