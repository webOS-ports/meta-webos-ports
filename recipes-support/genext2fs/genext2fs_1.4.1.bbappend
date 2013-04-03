# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

PRINC := "${@int(PRINC) + 1}"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://fastSymlink-webos.patch "

BBCLASSEXTEND = "native"
