# Copyright (c) 2014-2024 LG Electronics, Inc.

SUMMARY = "webOS Configuration Service data"
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

#WEBOS_VERSION = "1.0.0-2_26762505fae3b2b7c2603d85ee05712abd647897"
#PR = "r2"

inherit webos_ports_ose_repo

PV = "1.0.0-2+git"
SRCREV = "50632e28100688651f8938c809c0a81719fdb626"
PR = "r2"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${sysconfdir}/configd
    cp -vf ${S}/configs/layers.json ${D}${sysconfdir}/configd
    install -d ${D}${sysconfdir}/configd/layers/base
    install -m 0644 ${S}/configs/layers/base/com.webos.surfacemanager.json ${D}${sysconfdir}/configd/layers/base
    # The per-machine file is only load-bearing where nothing works these
    # values out at runtime. On a Halium machine luneos-device-config derives
    # the geometry from the DRM connector and the pixel ratio from the panel
    # width, and feeds them through the configd overlay layer, which outranks
    # base - so the file below is inert there. It stays for the machines that
    # have no such derivation (the mainline and qemu targets), and those are
    # the ones to convert before this whole per-machine tree can go.
    if [ -f ${S}/configs/layers/base/${MACHINE}/com.webos.surfacemanager.json ]
    then
        install -m 0644 ${S}/configs/layers/base/${MACHINE}/com.webos.surfacemanager.json ${D}${sysconfdir}/configd/layers/base
    fi
}
