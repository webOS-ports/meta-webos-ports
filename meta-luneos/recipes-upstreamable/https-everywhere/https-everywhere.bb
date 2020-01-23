# Copyright (c) 2016-2019 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "HTTPS Everywhere Database to be used in LuneOS Browser App"

LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM += "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_filesystem_paths python3native
DEPENDS = "python3-lxml-native"

PV = "2019.11.7+git${SRCPV}"
SRCREV = "a9c798759ee53c88d1b2abbf305af0ba3f6b286f"

SRC_URI = " \
    git://github.com/EFForg/https-everywhere.git;protocol=git;branch=release \
    file://make-sqlite.py \
"
S = "${WORKDIR}/git"

do_compile() {
    python3 ${WORKDIR}/make-sqlite.py
}

do_install() {
    # copy the created json database with rulesets to the browser app
    install -d ${D}${webos_applicationsdir}/org.webosports.app.browser/qml/utils/
    cp -rv ${WORKDIR}/rulesets.sqlite ${D}${webos_applicationsdir}/org.webosports.app.browser/qml/utils/
}

FILES_${PN} = "${webos_applicationsdir}/org.webosports.app.browser/qml/utils/rulesets.sqlite"
