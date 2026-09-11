# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "Sleep scheduling policy daemon"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "nyx-lib luna-service2 json-c libxml2 sqlite3 glib-2.0"
#Added for LuneOS
RDEPENDS:${PN} += "com.webos.service.battery"

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: what used to be the
# 0001-0014 patch series, sleepd.conf and sleepd.service in this directory now
# lives as commits there, along with the static-analysis and hardening cleanup
# on top of it, so nothing is applied here any more. Pinned with a plain
# SRCREV - submission tags are a webosose convention and this branch carries
# none. The branch itself comes from webos_ports_ose_repo below.
SRCREV = "01c6a8465a639231201ffc4a80f3f8e2dbec01f7"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 2.0.0-19, so anything lower
# would look like a downgrade to opkg on an update.
PV = "2.0.0-20"

PR = "r15"

inherit webos_component
# The cleanup and hardening work (webOS-ports/sleepd#1) lives on
# herrie/cleanup, not on the branch webos_ports_ose_repo defaults to, so the
# SRCREV above is not reachable from webOS-ports/webOS-OSE. Drop this line
# once herrie/cleanup is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/cleanup"
inherit webos_ports_ose_repo
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
LUNEOS_SYSTEMD_SERVICE = "sleepd.service"
