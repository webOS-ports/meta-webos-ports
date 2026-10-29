SUMMARY = "Photos & Videos: the pictures and videos on the device and in online albums"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=db37b6ead2dec60580595736ff983452"

PV = "1.0.0+git"
SRCREV = "773ff0059bd27e260868d1ceb40d18603c6f7355"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths
inherit webos_app

# A QML app with no C++ of its own (it was Enyo 2): installed by copying, as
# the Videos recipe does.
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${webos_applicationsdir}/${BPN}
    cd ${S}
    cp -R --no-dereference --preserve=mode,links appinfo.json icon.png icon-256x256.png COPYING qml \
        ${D}${webos_applicationsdir}/${BPN}/
}

FILES:${PN} += "${webos_applicationsdir}/${BPN}"

# The QML modules it imports: QtQuick, QtQuick.Controls, QtCore; QtMultimedia;
# Eos.Window; LunaNext.Common, LuneOS.* and QtQuick.Controls.LuneOS.
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    qtmultimedia-qmlplugins \
    qml-webos-framework \
    luneos-components \
"
