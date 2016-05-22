# Copyright (c) 2016 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "HTTPS Everywhere Database to be used in LuneOS Browser App"

LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM += "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_filesystem_paths pythonnative
DEPENDS = "python-lxml-native"

SRCREV = "54c5c953560ad9c42479c9acd1f5d092ac9734e7"

SRC_URI = "git://github.com/EFForg/https-everywhere.git;protocol=git;branch=master"
S = "${WORKDIR}/git"

do_compile() {
    #the python script expects the ../pkg dir to exist. We can either patch the python script or simple create the pkg dir. The latter is far easier and failsafe :)
    mkdir -p ${S}/pkg
    python ${S}/utils/make-json.py
}

do_install() {
    # copy the created json database with rulesets to the browser app
    install -d ${D}${webos_applicationsdir}/org.webosports.app.browser/qml/Utils/
    cp -rv ${S}/pkg/rulesets.json ${D}${webos_applicationsdir}/org.webosports.app.browser/qml/Utils/
}

FILES_${PN} = "${webos_applicationsdir}/org.webosports.app.browser/qml/Utils/rulesets.json"
