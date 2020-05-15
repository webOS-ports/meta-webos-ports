# Copyright (c) 2012-2014 LG Electronics, Inc.

SUMMARY = "Initialization, setup, and font files used by luna-sysmgr and luna-sysservice"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "tzdata python3-pytz-native"

PV = "2.0.1-10+git${SRCPV}"
SRCREV = "9f0d2b6e317024300dcd00c86f516083e302d4bc"

inherit allarch
inherit webos_ports_fork_repo
inherit webos_filesystem_paths
inherit webos_cmake
inherit python3native

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-Use-python3.patch \
"
S = "${WORKDIR}/git"

do_install_append() {
    # Expand fonts tarball
    if [ -e ${S}/files/conf/fonts/fonts.tgz ]; then
        install -d ${D}${datadir}/fonts
        tar xvzf ${S}/files/conf/fonts/fonts.tgz --directory=${D}${datadir}/fonts
        chown -R root:root ${D}${datadir}/fonts
    fi
    install -d ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/locale.txt ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/defaultPreferences.txt ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/default-launcher-page-layout.json ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/default-dock-positions.json ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/CustomerCareNumber.txt ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/src/mccInfo.json ${D}${webos_sysconfdir}
	
    install -d ${D}${webos_sysmgr_datadir}/customization
    install -v -m 644 ${S}/files/conf/cust-preferences.txt ${D}${webos_sysmgr_datadir}/customization

}

PACKAGES =+ "${PN}-fonts"
FILES_${PN} += "${webos_prefix} ${webos_sysconfdir} ${webos_sysmgr_datadir}/customization"
FILES_${PN}-fonts += "${datadir}/fonts/"
