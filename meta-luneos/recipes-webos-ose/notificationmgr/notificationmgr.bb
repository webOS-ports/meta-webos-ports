# Copyright (c) 2013-2025 LG Electronics, Inc.

DESCRIPTION = "Notification Manager"
AUTHOR = "Rajesh Gopu I.V <rajeshgopu.iv@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib boost libxml++-5.0 glibmm"

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: what used to be the nine
# patches in this directory now lives as commits there, along with the
# herrie/fixes audit on top of them (memory safety, db8 query injection,
# privilege prefix matching, displayId bounds, queue liveness) and its test
# harness. Pinned with a plain SRCREV - submission tags are a webosose
# convention and this branch carries none. The branch itself comes from
# webos_ports_ose_repo below.
SRCREV = "6f10d54cf398c6eae6fd17a92219dbfba4d35800"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 1.0.0-28, so anything lower
# would look like a downgrade to opkg on an update.
PV = "1.0.0-29"

PR = "r14"

inherit webos_component
inherit webos_cmake
inherit webos_daemon
inherit webos_system_bus
# The audit and test harness work lives on herrie/fixes, not on the branch
# webos_ports_ose_repo defaults to, so the SRCREV above is not reachable from
# webOS-ports/webOS-OSE. Drop this line once that branch is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_ose_repo

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "notificationmgr.service.in"

# All service files will be managed in meta-lg-webos.
# The service file in the repository is not used, so please delete it.
# See the page below for more details.
# http://collab.lge.com/main/pages/viewpage.action?pageId=2031668745
do_install:append() {
    rm ${D}${sysconfdir}/systemd/system/notificationmgr.service
}

FILES:${PN} += "${webos_prefix}"
