# Copyright (c) 2012-2014 LG Electronics, Inc.

#
# NOTE: Keep the lists in alphabetical order
#

# Newer upstream meta-oe already has yajl 2.0.3, and we should upgrade to it soon,
# because it's already compatible with our libpbnjson.
PREFERRED_VERSION_yajl ?= "1.0.12"

QT5_VERSION = "5.4.0+git%"
require conf/distro/include/qt5-versions.inc

PREFERRED_VERSION_ofono = "1.14+gitr%"
