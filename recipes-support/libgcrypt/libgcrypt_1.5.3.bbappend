# Copyright (c) 2012-2013 LG Electronics, Inc.

EXTRA_OECONF =+ " --disable-static"

do_configure_append() {
    sed -i '/^SUBDIRS/s/ doc tests//' Makefile
}

