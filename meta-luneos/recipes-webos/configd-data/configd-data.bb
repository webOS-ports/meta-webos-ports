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
SRCREV = "8b41d8c3fd7f3e64fb9f18d4db25a223b42a191e"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}${sysconfdir}/configd
    cp -vrf ${S}/configs/* ${D}${sysconfdir}/configd
}
