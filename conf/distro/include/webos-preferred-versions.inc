# Copyright (c) 2012-2014 LG Electronics, Inc.

#
# NOTE: Keep the lists in alphabetical order
#

# Newer upstream meta-oe already has yajl 2.0.3, and we should upgrade to it soon,
# because it's already compatible with our libpbnjson.
PREFERRED_VERSION_yajl ?= "1.0.12"

PREFERRED_VERSION_qtwebkit = "5.2.1+git%"

# We have to stick with our git version of ofono and can't use any newer upstream
# version from oe-core
PREFERRED_VERSION_ofono = "1.12+git%"
