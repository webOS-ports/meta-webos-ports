SUMMARY = "Authenticator: one-time codes for two-step verification, kept in the key manager"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=db37b6ead2dec60580595736ff983452"

PV = "1.0.0+git"
SRCREV = "b85bb2959ef22aa8fc6d50e63d230ab9c55d4edc"
# b85bb295 is on herrie/lc-shared-parts (org.webosports.app.authenticator PR #3), not yet merged into master; back to that branch once it is.
WEBOS_GIT_PARAM_BRANCH = "herrie/lc-shared-parts"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_app

# QML and JavaScript only: nothing to build. webos_app writes the role and
# client permissions from appinfo.json's requiredPermissions.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${webos_applicationsdir}/${BPN}
    cd ${S}
    cp -R --no-dereference --preserve=mode,links appinfo.json icon.png icon-256x256.png LICENSE qml \
        ${D}${webos_applicationsdir}/${BPN}/
}

FILES:${PN} += "${webos_applicationsdir}/${BPN}"

# QtQuick, Controls, Layouts and Shapes (qtdeclarative-qmlplugins);
# LuneOS.Components, Forms, Passcode, Service and System, LunaNext.Common and
# the LuneOS Controls style (luneos-components). The codes' secrets are kept
# in com.palm.keymanager.
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    luneos-components \
    com.palm.keymanager \
"
