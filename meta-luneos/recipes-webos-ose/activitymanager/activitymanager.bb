# Copyright (c) 2012-2024 LG Electronics, Inc.

DESCRIPTION = "webOS component to manage all running activities."
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/dameons"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "luna-service2 db8 boost libpbnjson glib-2.0 pmloglib ${VIRTUAL-RUNTIME_init_manager}"

WEBOS_VERSION = "3.0.0-43_5a951fe6d353ea84fbccc265f1ead1cbf4327ff0"
PR = "r16"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Allow-for-1-minute-intervals.patch \
    file://0002-Revert-Retire-the-deprecated-APIs.patch \
    file://0003-activitymanager-Fix-outbound-permissions-to-com.webo.patch \
    file://0001-CMakeLists.txt-replace-std-c-11-with-std-c-17-for-ic.patch \
"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "activitymanager.service"

FILES:${PN} += "${webos_sysbus_datadir}"
FILES:${PN} += "${localstatedir}/lib/activitymanager"

EXTRA_OECMAKE += "-DINIT_MANAGER:STRING='${@bb.utils.filter('VIRTUAL-RUNTIME_init_manager', 'systemd upstart', d)}'"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'webos-dac', d)}"
PACKAGECONFIG[webos-dac] = "-DDAC_IMPLEMENTATION:BOOL=TRUE,,"

do_install:append() {
        install -m 0700 -o system -g system -v -d ${D}${localstatedir}/lib/activitymanager
}
