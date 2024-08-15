SUMMARY = "Application to start and monitor the update of the WebOS-Ports system"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "qtbase qtdeclarative"
RDEPENDS:${PN} += "qtdeclarative-qmlplugins"

PV = "1.0.0-4+git"
SRCREV = "a07b22bc8a47fb3160c9eeb8a2aafee19d750d8f"

inherit webos_ports_repo

inherit qt6-qmake

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "webos-system-update.service"

do_install() {
    install -d ${D}${sbindir}
    install -m 0777 ${B}/webos-system-update ${D}${sbindir}

    install -d ${D}${bindir}
    install -m 0777 ${S}/files/scripts/run-update.sh ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/files/systemd/webos-system-update.service ${D}${systemd_unitdir}/system/
}

# ERROR: webos-system-update-1.0.0-4+git-r0 do_package_qa: QA Issue: File /usr/src/debug/webos-system-update/1.0.0-4+git/qrc_resources.cpp in package webos-system-update-src contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
