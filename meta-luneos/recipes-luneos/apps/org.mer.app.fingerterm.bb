SUMMARY = "A terminal emulator with a custom virtual keyboard"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SPV = "1.3.6"
PV = "${SPV}+git"
SRCREV = "17f371ad2147d537ffa4559a5453794cd32c7d36"
WEBOS_REPO_NAME = "fingerterm"
WEBOS_GIT_PARAM_BRANCH = "herrie/qt6"

DEPENDS = "qtbase qtdeclarative qttools-native qt5compat"
RDEPENDS:${PN} = "ttf-liberation-mono"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://appinfo.json \
"

EXTRA_QMAKEVARS_PRE = "\
    DEFAULT_FONT=LiberationMono \
    DEPLOYMENT_PATH=/usr/palm/applications/${PN} \
"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit qt6-qmake

APP_PATH = "${webos_applicationsdir}/${PN}"

do_configure:append() {
    sed -i -e s:/usr/bin/${PN}:${APP_PATH}/${PN}:g ${S}/*.cpp
}

do_install:append() {
    install -d ${D}${APP_PATH}

    install -m 0644 ${UNPACKDIR}/appinfo.json ${D}${APP_PATH}
    install -m 0755 ${B}/fingerterm ${D}${APP_PATH}
    install -m 0644 ${S}/fingerterm.png ${D}${APP_PATH}/icon.png

    # Always provide same version as we have in our recipe
    sed -i -e s:__VERSION__:${SPV}:g ${D}${APP_PATH}/appinfo.json
}

FILES:${PN} += "${APP_PATH} ${datadir}/translations"

# ERROR: org.mer.app.fingerterm-1.3.6+git-r0 do_package_qa: QA Issue: File /usr/src/debug/org.mer.app.fingerterm/1.3.6+git/qrc_resources.cpp in package org.mer.app.fingerterm-src contains reference to TMPDIR [buildpaths]
ERROR_QA:remove = "buildpaths"
WARN_QA:append = " buildpaths"
