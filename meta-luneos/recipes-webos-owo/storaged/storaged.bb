# Copyright (c) 2012-2013 LG Electronics, Inc.

SUMMARY = "Mass Storage Mode Manager"
SECTION = "webos/base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "nyx-lib luna-service2 json-c glib-2.0"
RDEPENDS:${PN} = "bash"

# The volume space and encryption reporting (webOS-ports/storaged#12) lives
# on herrie/volumes-space-encryption, not on the branch webos_ports_ose_repo
# defaults to, so the SRCREV below is not reachable from
# webOS-ports/webOS-OSE. Drop this line once that branch is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/volumes-space-encryption"
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_system_bus
inherit webos_systemd
inherit pkgconfig

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

WEBOS_SYSTEM_BUS_SKIP_DO_TASKS = "1"
WEBOS_SYSTEM_BUS_FILES_LOCATION = "${S}/files/sysbus"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

PV = "2.1.0-5+git"
SRCREV = "70de922e58dec07e730a3a5f32988d27a1f84c6f"
