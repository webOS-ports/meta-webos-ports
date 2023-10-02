# Copyright (c) 2012-2023 LG Electronics, Inc.

SUMMARY = "webOS logging library"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 libpbnjson"

WEBOS_VERSION = "3.3.0-7_70ff1081b4ff6d910b89b96c86c6e42a5fa29c6a"
PR = "r10"

PV = "3.3.0-7+git"
SRCREV = "70ff1081b4ff6d910b89b96c86c6e42a5fa29c6a"

LEAD_SONAME = "libPmLogLib.so"
EXTRA_OECMAKE += "-DWEBOS_DISTRO_PRERELEASE:STRING='${WEBOS_DISTRO_PRERELEASE}'"

inherit webos_public_repo
inherit webos_cmake
inherit webos_pmlog_config
inherit pkgconfig

inherit webos_systemd

WEBOS_SYSTEMD_SERVICE = "pm-log-daemon.service"
WEBOS_SYSTEMD_SCRIPT = "pm-log-daemon-stop.sh.in"

PACKAGECONFIG ??= ""
PACKAGECONFIG[whitelist] = "-DENABLE_WHITELIST:BOOL=TRUE, -DENABLE_WHITELIST:BOOL=FALSE"

SRC_URI = "${WEBOSOSE_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"
