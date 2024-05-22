# Copyright (c) 2012-2024 LG Electronics, Inc.

SUMMARY = "libsandbox is a collection of APIs for separating running programs"
AUTHOR = "Vibhanshu Dhote <vibhanshu.dhote@lge.com>"
SECTION = "webos/devel"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=629222c36a9c68f9e1855667faef01ae \
"

WEBOS_VERSION = "2.0.0-2_03120c12ebae2cb2fbd4cb2b58e6b2c6b565efa5"
PR = "r3"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
           file://0001-Remove-webos_machine_impl_dep.patch \
"
S = "${WORKDIR}/git"

ALLOW_EMPTY:${PN} = "1"
