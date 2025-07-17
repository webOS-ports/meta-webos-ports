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

WEBOS_VERSION = "2.0.1-11_b186c185d8304480e78aade0617ef795c3db3e6e"
PR = "r20"

inherit webos_arch_indep
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit python3native

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
        file://0001-luna-init-Add-cust-preferences.txt-and-CustomerCareN.patch \
        file://0002-luna-init-Add-default-launcher-page-layout.json.patch \
        file://0003-default-dock-positions.json-Use-variant-for-LuneOS.patch \
        file://0004-defaultPreferences.txt-Customize-ringtone-and-wallpa.patch \
        file://0005-defaultPreferences.txt-Add-keyboard-key-used-by-Mali.patch \
        file://0006-command-resource-handlers.json-Switch-to-new-variant.patch \
        file://0007-command-resource-handlers.json-Add-custom-handlers-f.patch \
        file://0008-gen-ext-timezones.py-Update-for-python3.patch \
        file://0009-CMakeLists.txt-Fix-install-location-of-files.patch \
        file://0010-CMakeLists.txt-Install-mccInfo.json-as-well.patch \
        file://0011-Delete-ext-timezones.json.patch \
        file://0012-luna-init-gen-ext-timezones.py-Fix-typo.patch \
"

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
