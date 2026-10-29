SUMMARY = "Videos: finds the videos on the device and plays them, with subtitles"
SECTION = "webos/apps"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=db37b6ead2dec60580595736ff983452"

PV = "1.0.0+git"
SRCREV = "2a00b53e81105f562f4a94db851526c86698379a"

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

# QtQuick, Controls, Layouts and Qt.labs.folderlistmodel
# (qtdeclarative-qmlplugins), QtMultimedia, QtSensors, QOfono (libqofono),
# Eos.Window (qml-webos-framework), and LuneOS.Components, Forms, Service
# and System, LunaNext.Common and the LuneOS Controls style
# (luneos-components).
RDEPENDS:${PN} = " \
    qtdeclarative-qmlplugins \
    qtmultimedia-qmlplugins \
    qtsensors-qmlplugins \
    libqofono \
    qml-webos-framework \
    luneos-components \
"
