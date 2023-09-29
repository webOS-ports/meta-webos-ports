# Copyright (c) 2012-2023 LG Electronics, Inc.

SUMMARY = "webOS preferences manager"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "luna-service2 json-c sqlite3 glib-2.0 nyx-lib"
RDEPENDS:${PN} = "luna-prefs-data"

WEBOS_VERSION = "3.0.0-14_5470fb26fccbd246a23bb519c3b1050d6d305562"

PV = "3.0.0-14+git${SRCPV}"
SRCREV = "5470fb26fccbd246a23bb519c3b1050d6d305562"

inherit webos_public_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

inherit features_check
# luna-service2 depends on systemd
REQUIRED_DISTRO_FEATURES = "systemd"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

