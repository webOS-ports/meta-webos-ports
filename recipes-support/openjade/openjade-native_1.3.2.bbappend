# Copyright (c) 2013 LG Electronics, Inc.

PRINC := "${@int(PRINC) + 1}"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://Makefile.prog.in.patch \
"
