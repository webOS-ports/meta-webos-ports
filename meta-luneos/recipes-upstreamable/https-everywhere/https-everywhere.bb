# Copyright (c) 2016-2019 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "HTTPS Everywhere Database to be used in LuneOS Browser App"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM += "file://${COMMON_LICENSE_DIR}/GPL-3.0-only;md5=c79ff39f19dfec6d293b95dea7b07891"

inherit webos_filesystem_paths python3native
DEPENDS = "python3-lxml-native"

PV = "2021.7.13+git${SRCPV}"
SRCREV = "7925254112f9cd9ee31c8c36c63bf18d8e6fbaf9"

SRC_URI = " \
    git://github.com/EFForg/https-everywhere.git;protocol=https;branch=release \
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

FILES:${PN} = "${webos_applicationsdir}/org.webosports.app.browser/qml/utils/rulesets.sqlite"
