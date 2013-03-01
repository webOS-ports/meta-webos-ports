# Copyright (c) 2012-2013 Hewlett-Packard Development Company, L.P.

PRINC := "${@int(PRINC) + 2}"

SRC_URI += "file://removedoc-webos.patch"
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
