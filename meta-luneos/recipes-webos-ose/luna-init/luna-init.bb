# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "Initialization, setup, and font files used by luna-sysmgr and luna-sysservice"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "tzdata python3-pytz-native"

SRCREV = "980077069ce7438bcc1bb0dfe96f8601ad7fc283"

PR = "r22"

inherit webos_arch_indep
inherit webos_ports_ose_repo
inherit webos_cmake
inherit python3native

PV = "2.0.1-12"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install:append() {
    # Expand fonts tarball
    if [ -e ${S}/files/conf/fonts/fonts.tgz ]; then
        install -d ${D}${datadir}/fonts
        tar xvzf ${S}/files/conf/fonts/fonts.tgz --directory=${D}${datadir}/fonts
        chown -R root:root ${D}${datadir}/fonts
    fi
    install -d ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/locale.txt ${D}${webos_sysconfdir}
}

PACKAGES =+ "${PN}-fonts"
FILES:${PN} += "${webos_prefix} ${webos_sysconfdir}"
# Below is needed for the LuneOS additions
FILES:${PN} += "${webos_sysmgr_datadir}/customization/"
FILES:${PN}-fonts += "${datadir}/fonts/"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
