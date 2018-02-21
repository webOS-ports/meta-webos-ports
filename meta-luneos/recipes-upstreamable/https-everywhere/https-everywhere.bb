# Copyright (c) 2016 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "HTTPS Everywhere Database to be used in LuneOS Browser App"

LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM += "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_filesystem_paths pythonnative
DEPENDS = "python-lxml-native"

SRCREV = "b7306d32a7485d135614cec8be1705646a20cc30"

SRC_URI = " \
    git://github.com/EFForg/https-everywhere.git;protocol=git;branch=master \
    file://make-sqlite.py \
"
S = "${WORKDIR}/git"

do_compile() {
    python ${WORKDIR}/make-sqlite.py
}

do_install() {
    # copy the created json database with rulesets to the browser app
    install -d ${D}${webos_applicationsdir}/org.webosports.app.browser/qml/utils/
    cp -rv ${WORKDIR}/rulesets.sqlite ${D}${webos_applicationsdir}/org.webosports.app.browser/qml/utils/
}

FILES_${PN} = "${webos_applicationsdir}/org.webosports.app.browser/qml/utils/rulesets.sqlite"
