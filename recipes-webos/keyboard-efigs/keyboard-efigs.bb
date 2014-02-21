# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Open webOS default virtual keyboard"
SECTION = "webos/libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "qt4-webos glib-2.0 luna-prefs luna-service2 luna-webkit-api libpbnjson"

WEBOS_VERSION = "3.0.0-2_189da3d92122c19aae9fa2343a7977c972b2e6bb"

inherit webos_public_repo
inherit webos_qmake
inherit webos_enhanced_submissions
inherit webos_library
inherit webos_machine_dep

SRC_URI = "${OPENWEBOS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit webos-ports-submissions
SRCREV = "fc69babb308f11ee3f42334af393f51a2d9be198"

EXTRA_OEMAKE += "MACHINE=${MACHINE}"

do_configure() {
    MACHINE=${MACHINE} ${QMAKE}
}

do_install() {
    oe_runmake INSTALL_ROOT=${D} install
}

FILES_${PN} += "${webos_sysmgr_datadir}"

# libkeyboard-efigs-phone.so and libkeyboard-efigs-tablet.so are already stripped
INSANE_SKIP_${PN} = "already-stripped"
# qemuarm also has bad RPATHs
INSANE_SKIP_${PN}_append_qemuarm = " rpaths"
