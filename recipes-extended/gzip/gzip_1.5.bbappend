# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.
# Copyright (c) 2013 LG Electronics, Inc.

PRINC := "${@int(PRINC) + 1}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${P}:"

SRC_URI += "file://removedoc-webos.patch"
