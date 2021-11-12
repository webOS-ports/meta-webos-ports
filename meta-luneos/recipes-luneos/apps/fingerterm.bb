SUMMARY = "A terminal emulator with a custom virtual keyboard"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PV = "1.3.6+git${SRCPV}"
SRCREV = "64a3554969747e348bfa9eb85842490cef9b719e"

DEPENDS = "qtbase qtdeclarative"
RDEPENDS:${PN} = "ttf-liberation-mono qtxmlpatterns"

SRC_URI = " \
    git://github.com/sailfishos/fingerterm.git;protocol=https;branch=master \
    file://appinfo.json \
"
S = "${WORKDIR}/git"

EXTRA_QMAKEVARS_PRE = "\
    DEFAULT_FONT=LiberationMono \
    DEPLOYMENT_PATH=/usr/palm/applications/${PN} \
"

inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit qmake5

APP_PATH = "${webos_applicationsdir}/${PN}"

do_configure:append() {
    sed -i -e s:/usr/bin/${PN}:${APP_PATH}/${PN}:g ${S}/*.cpp
}

do_install:append() {
    install -d ${D}${APP_PATH}

    install -m 0644 ${WORKDIR}/appinfo.json ${D}${APP_PATH}
    install -m 0755 ${WORKDIR}/build/fingerterm ${D}${APP_PATH}
    install -m 0644 ${S}/fingerterm.png ${D}${APP_PATH}/icon.png

    # Always provide same version as we have in our recipe
    sed -i -e s:__VERSION__:${PV}:g ${D}${APP_PATH}/appinfo.json
}

FILES:${PN} += "${APP_PATH}"
