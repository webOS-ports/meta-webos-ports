# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://fastSymlink-webos.patch "

BBCLASSEXTEND = "native"
