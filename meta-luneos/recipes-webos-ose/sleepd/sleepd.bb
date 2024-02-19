# Copyright (c) 2012-2024 LG Electronics, Inc.

SUMMARY = "Sleep scheduling policy daemon"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "nyx-lib luna-service2 json-c libxml2 sqlite3 glib-2.0"
RDEPENDS:${PN} += "com.webos.service.battery"

WEBOS_VERSION = "2.0.0-17_3727287956ca9ac3c6dfb05c64a2df446fa61289"
PR = "r11"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE} \
    file://0001-Add-empty-alarms.xml-file.patch \
    file://0002-Add-alarms.xml-to-CMakeLists.txt.patch \
    file://0003-Use-com.palm.display-service-to-query-display-state.patch \
    file://0004-Rework-suspend-state-machine-to-support-asynchronous.patch \
    file://0005-Unblock-us-from-being-not-responsible-and-fixing-a-c.patch \
    file://0006-Don-t-remove-idle-check-from-mainloop-when-currently.patch \
    file://0007-Don-t-block-main-thread-when-in-sleep-state.patch \
    file://0008-Don-t-handle-displayInactive-event.patch \
    file://0009-Creating-activities-while-being-suspend-will-wakeup-.patch \
    file://0010-Add-powerd.management-permission-needed-by-powermenu.patch \
    file://0011-Revert-Deprecation-of-com.webos.service.power.patch \
    file://0012-com.webos.service.sleep.api.json.in-Add-API-for-susp.patch \
"
S = "${WORKDIR}/git"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "sleepd.service"
