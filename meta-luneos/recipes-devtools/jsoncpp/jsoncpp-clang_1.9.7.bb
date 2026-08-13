# Copyright (c) 2023 LG Electronics, Inc.

require recipes-devtools/jsoncpp/jsoncpp_${PV}.bb
# BPN here is jsoncpp-clang, so FILESPATH never looks in meta-oe's jsoncpp dirs.
# 1.9.7 pulls two patches from recipes-devtools/jsoncpp/jsoncpp and run-ptest
# from recipes-devtools/jsoncpp/files. Same idiom as googletest-clang.
FILESEXTRAPATHS:prepend = "${META_OE_LAYER}/recipes-devtools/jsoncpp/jsoncpp:${META_OE_LAYER}/recipes-devtools/jsoncpp/files:"

require jsoncpp-clang.inc
