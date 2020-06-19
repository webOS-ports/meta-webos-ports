# Copyright (c) 2013-2019 LG Electronics, Inc.

SUMMARY = "Remote diagnostics daemon and utilities"
AUTHOR = "Gayathri Srinivasan <gayathri.srinivasan@lge.com>"
SECTION = "base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "glib-2.0 libpbnjson luna-prefs luna-service2 pmloglib"
#Add tar dependency since --absolute-names support is missing in busybox tar
RDEPENDS_${PN} = "nyx-utils tar"

PROVIDES = "librdx rdx-utils"

inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_system_bus
inherit pkgconfig
inherit webos_systemd

PV = "4.0.2-3+git${SRCPV}"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git"

SRCREV = "f159cb54d13777e835015bdbcdf5be46e58fe539"

EXTRA_OECMAKE += "-DWEBOS_USE_LEGACY_PACKAGE_MANAGER:BOOL=FALSE"

VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS_${PN}_append_class-target = " ${VIRTUAL-RUNTIME_bash}"
