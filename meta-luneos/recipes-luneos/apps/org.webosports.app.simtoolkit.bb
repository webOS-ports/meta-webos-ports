SUMMARY = "SIM Toolkit: the SIM card's own menus and prompts, through oFono"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=db37b6ead2dec60580595736ff983452"

PV = "0.1.0+git"
SRCREV = "ee5657a3a56671555db8565eb8ab4e619c846862"

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

# QtQuick, Controls and Layouts (qtdeclarative-qmlplugins); LuneOS.Forms,
# Service and System, LunaNext.Common and the LuneOS Controls style
# (luneos-components).
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    luneos-components \
"
