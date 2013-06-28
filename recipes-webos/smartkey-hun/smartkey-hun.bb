# Copyright (c) 2012 Hewlett-Packard Development Company, L.P.
# Copyright (c) 2013 LG Electronics, Inc.

DESCRIPTION = "Implementation of the Open webOS SmartKey spell checking service using hunspell"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "libpbnjson cjson glib-2.0 luna-service2 icu hunspell"

# corresponds to tag submissions/4
SRCREV = "75a4ea405b600c179f13a2633e81371c69017fab"
PV = "3.0.0-4"

#Uncomment once do_install() has been moved out of the recipe
inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus
inherit webos_daemon
inherit webos_qmake

WEBOS_GIT_TAG = "submissions/${WEBOS_SUBMISSION}"
SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

EXTRA_OEMAKE += "PLATFORM=${TARGET_ARCH}"

do_configure(){
    ${QMAKE}
}

do_install() {
    oe_runmake INSTALL_DIR=${D} install
}
