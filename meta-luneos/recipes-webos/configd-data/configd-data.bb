# Copyright (c) 2014-2021 LG Electronics, Inc.

SUMMARY = "webOS Configuration Service data"
AUTHOR  = "SangWook Han <sangwook.han@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

inherit webos_ports_ose_repo

PV = "1.0.0-2+git${SRCPV}"
SRCREV = "03288af68004d4036e8ec4099e1efd8ee449f243"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${sysconfdir}/configd
    cp -vf ${S}/configs/layers.json ${D}${sysconfdir}/configd
    install -d ${D}${sysconfdir}/configd/layers/base
    install -m 0644 ${S}/configs/layers/base/com.webos.surfacemanager.json ${D}${sysconfdir}/configd/layers/base
    if [ -f ${S}/configs/layers/base/${MACHINE}/com.webos.surfacemanager.json ]
    then
        install -m 0644 ${S}/configs/layers/base/${MACHINE}/com.webos.surfacemanager.json ${D}${sysconfdir}/configd/layers/base
    fi
}
