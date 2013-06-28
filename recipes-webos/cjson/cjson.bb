# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.
# Copyright (c) 2013 LG Electronics, Inc.

SUMMARY = "Open webOS edition of the open-source json-c library"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit webos_autotools
inherit webos_enhanced_submissions
inherit webos_library
inherit webos_public_repo

# corresponds to tag submissions/35
SRCREV = "43de024867b8d38f607f04cfede8608ce532a8e6"
PV = "1.8.0-35"

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

EXTRA_OECONF += "--disable-static"

EXTRA_OEMAKE += "all"

do_configure_prepend() {
    # Force a configure to happen
    rm -f ${S}/config.status
    sh autogen.sh
}
