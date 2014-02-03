SUMMARY = "Application to start and monitor the update of the WebOS-Ports system"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "qtbase qtdeclarative"
RDEPENDS_${PN} += "qtdeclarative-qmlplugins"

WEBOS_VERSION = "1.0.0-2_8c957e4b872d0d8cbe63096ca0daab216acb689f"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions

inherit qmake5

# Set path of qt5 headers as qmake5_base.bbclass sets this to just ${includedir} but
# actually it is ${includedir}/qt5
OE_QMAKE_PATH_HEADERS = "${OE_QMAKE_PATH_QT_HEADERS}"

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "webos-system-update.service"

do_install() {
    install -d ${D}${sbindir}
    install -m 0777 ${B}/webos-system-update ${D}${sbindir}

    install -d ${D}${bindir}
    install -m 0777 ${S}/files/scripts/run-update.sh ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/webos-system-update.service ${D}${systemd_unitdir}/system/
}
