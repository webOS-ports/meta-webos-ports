# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

PRINC := "${@int(PRINC) + 2}"

EXTRA_OECONF =+ " --disable-static"

do_configure_append() {
    sed -i '/^SUBDIRS/s/ doc tests//' Makefile
}

