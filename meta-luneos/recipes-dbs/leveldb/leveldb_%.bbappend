# Copyright (c) 2017 LG Electronics, Inc.

# Needed for db8-native
BBCLASSEXTEND = "native"

PACKAGECONFIG:append = " snappy"

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://0001-Revert-Disable-exceptions-and-RTTI-in-CMake-configur.patch"
