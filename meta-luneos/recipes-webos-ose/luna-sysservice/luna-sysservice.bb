# Copyright (c) 2012-2025 LG Electronics, Inc.

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

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: what used to be the
# 0001-0009 patch series plus the CMake target_link_libraries fix in this
# directory now lives as commits there, so nothing is applied here any more.
# Pinned with a plain SRCREV - submission tags are a webosose convention and
# this branch carries none. The branch itself comes from webos_ports_ose_repo
# below.
SRCREV = "c2f38bf1fd71f9cc35ce983aa548aefdb8aaa4bf"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 4.4.0-31, so anything lower
# would look like a downgrade to opkg on an update.
PV = "4.4.0-32"

PR = "r17"

inherit webos_component
# The code audit work lives on herrie/fixes, not on the branch
# webos_ports_ose_repo defaults to, so the SRCREV above is not reachable from
# webOS-ports/webOS-OSE. Drop this line once herrie/fixes is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_ose_repo
inherit webos_system_bus
inherit webos_daemon
inherit webos_cmake

PACKAGECONFIG ??= "qt"
PACKAGECONFIG[qt] = ",,qtbase"
inherit_defer ${@bb.utils.contains('PACKAGECONFIG', 'qt', 'qt6-cmake', '', d)}

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
# The unit ships in the repository (files/systemd/), like the other LuneOS
# components, rather than being injected from this directory.
LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

do_install:append() {
    install -d ${D}${datadir}/localization/${BPN}
    cp -rf ${S}/resources ${D}/${datadir}/localization/${BPN}
    # FIXME: We still need this or registration fails
    rm -rf ${D}${webos_sysbus_prvrolesdir}/com.webos.*
    rm -rf ${D}${webos_sysbus_pubrolesdir}/com.webos.*
}

FILES:${PN} += "${datadir}/localization/${BPN}"
