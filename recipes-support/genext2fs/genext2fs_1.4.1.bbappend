# Copyright (c) 2012-2013 LG Electronics, Inc.

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " file://fastSymlink-webos.patch "

BBCLASSEXTEND = "native"
