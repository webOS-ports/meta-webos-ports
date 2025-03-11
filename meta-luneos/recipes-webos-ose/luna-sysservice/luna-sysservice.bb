# Copyright (c) 2012-2024 LG Electronics, Inc.

SUMMARY = "Provides preference, timezone and ringtone services"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=7bd705f8ae3d5077cbd3da7078607d8b \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

VIRTUAL-RUNTIME_ntp ?= "sntp"

DEPENDS = "luna-service2 libpbnjson uriparser libxml2 sqlite3 pmloglib nyx-lib libwebosi18n"

RDEPENDS:${PN} += "${VIRTUAL-RUNTIME_ntp} tzcode luna-init"

WEBOS_VERSION = "4.4.0-30_f943d3f051a60cfb249227709e9365ad4109bd9d"
PR = "r14"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus
inherit webos_daemon
inherit webos_cmake

# avoid qt6-cmake inherit - migrate this to inherit_defer after upgrading to scarthgap with:
# https://git.openembedded.org/openembedded-core/commit/?h=scarthgap&id=451363438d38bd4552d5bcec4a92332f5819a5d4
# https://git.openembedded.org/bitbake/commit/?h=2.8&id=5c2e840eafeba1f0f754c226b87bfb674f7bea29
# now it needs to be set in DISTRO config
#PACKAGECONFIG ??= "qt"
#PACKAGECONFIG[qt] = ",,qtbase"
#inherit_defer ${@bb.utils.contains('PACKAGECONFIG', 'qt', 'qt6-cmake', '', d)}

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Add-ImageService.patch \
    file://0002-luna-sysservice-Fix-spacing-issues.patch \
    file://0003-luna-sysservice-Add-required-bits-for-LuneOS.patch \
    file://0004-luna-sysservice-Fix-permissions-for-telephony.patch \
    file://0005-luna-sysservice-TimePrefsHandler.cpp-Fix-typo.patch \
    file://0006-com.webos.service.systemservice-Add-image.management.patch \
"

S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "luna-sys-service.service"

do_install:append() {
    install -d ${D}${datadir}/localization/${BPN}
    cp -rf ${S}/resources ${D}/${datadir}/localization/${BPN}
    # FIXME: We still need this or registration fails
    rm -rf ${D}${webos_sysbus_prvrolesdir}/com.webos.*
    rm -rf ${D}${webos_sysbus_pubrolesdir}/com.webos.* 
}

FILES:${PN} += "${datadir}/localization/${BPN}"
