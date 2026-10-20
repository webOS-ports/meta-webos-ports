SUMMARY = "A terminal emulator with a custom virtual keyboard"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SPV = "1.5.0"
PV = "${SPV}+git"
SRCREV = "5c34953368952e3475f8c4bf3c67d34407612c96"

DEPENDS = "qtbase qtdeclarative qttools-native"
RDEPENDS:${PN} = "ttf-liberation-mono"

SRC_URI = " \
    git://github.com/sailfishos/fingerterm.git;protocol=https;branch=master \
    file://appinfo.json \
"

EXTRA_QMAKEVARS_PRE = "\
    DEFAULT_FONT=LiberationMono \
    DEPLOYMENT_PATH=/usr/palm/applications/${PN} \
    VERSION_STRING=${SPV} \
"

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
