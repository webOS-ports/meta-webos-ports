# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "webOS preferences manager"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "luna-service2 json-c sqlite3 glib-2.0 nyx-lib"
RDEPENDS:${PN} = "luna-prefs-data"

WEBOS_VERSION = "3.0.0-22_c6aab21b1159ff70875149a57644e1ad34313489"
PR = "r18"

inherit webos_public_repo
# luna-prefs is mid-migration: it carries both the modern files/sysbus set
# (role.json.in, service.in, api.json, groups.json, installed by cmake) and the
# legacy service/*.{json,service}.{pub,prv} pair. webos_system_bus defaults
# WEBOS_SYSTEM_BUS_FILES_LOCATION to ${S}/service, so its deprecated path also
# installed the legacy pair into /usr/share/ls2/roles/{pub,prv} and
# /usr/share/dbus-1/{services,system-services}.
#
# That gave /usr/bin/luna-prefs-service three role files. RoleMap::Add() merges
# a legacy pub/prv pair, but not a modern role against a legacy one - it keeps
# whichever the hub reads first (readdir order) and logs "Role already exists"
# for the other. The legacy pair grants inbound/outbound "*" where the modern
# role is scoped, so which one wins actually matters.
#
# The modern set is complete, so point the deprecated path at nothing.
WEBOS_SYSTEM_BUS_FILES_LOCATION = ""

inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_program
inherit webos_library
inherit webos_system_bus

SRC_URI = " \
    ${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-luna-prefs-Fix-outbound-permissions.patch \
"
