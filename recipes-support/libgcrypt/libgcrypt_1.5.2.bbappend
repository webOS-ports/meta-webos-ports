# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

EXTRA_OECONF =+ " --disable-static"

do_configure_append() {
    sed -i '/^SUBDIRS/s/ doc tests//' Makefile
}

