# Copyright (c) 2013-2018 LG Electronics, Inc.

SUMMARY = "Bootd single-shot launching service"
DESCRIPTION = "Bootd is a simplified upstart-like component. It provides automatic single-shot launching at boot time"
AUTHOR = "Sangwoo Kang <sangwoo82.kang@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "systemd luna-service2 libpbnjson pmloglib glib-2.0 boost gtest"

PV = "0.1.0+git${SRCPV}"
SRCREV = "e5cc02571c464aef785749f840e77ebcdb77cf58"

inherit webos_cmake
inherit webos_system_bus
inherit webos_machine_impl_dep

SRC_URI = "\
    git://github.com/webosose/bootd.git \
    file://0001-Remove-PmTrace-dependency.patch \
"
S = "${WORKDIR}/git"

FILES_${PN} += "${webos_sysbus_datadir}"

# gtest option
PACKAGES =+ "${PN}-tests"
FILES_${PN}-tests = "${libexecdir}/tests/*"
