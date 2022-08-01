# Copyright (c) 2012-2018 LG Electronics, Inc.

SUMMARY = "NovaCOM -- Utility to communicate with an embedded device over USB"
AUTHOR = "Steve Lemke <steve.lemke@lge.com>"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README.md;beginline=71;md5=11b824a13dd500abb4097832ee662844"
SECTION = "webos/base"

PV = "1.1.0-18"
SRCREV = "d9d8b772b86231c6490ea8b1ff0e44ddaaf8df05"

inherit webos_public_repo
inherit webos_cmake
inherit deploy

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

BBCLASSEXTEND = "native"

do_deploy () {
    install -d ${DEPLOY_DIR_TOOLS}
    install -m 0755 novacom ${DEPLOY_DIR_TOOLS}
}
do_deploy[sstate-outputdirs] = "${DEPLOY_DIR_TOOLS}"
addtask deploy before do_build after do_package
