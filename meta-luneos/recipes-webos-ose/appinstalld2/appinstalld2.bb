# Copyright (c) 2017-2024 LG Electronics, Inc.

SUMMARY = "Application Install Service"
AUTHOR = "Guruprasad KN <guruprasad.kn@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "glib-2.0 luna-service2 libpbnjson pmloglib pmtrace boost icu"
# python3 is for luneos-app-permissions, see do_install:append below
RDEPENDS:${PN} = " \
    applicationinstallerutility \
    ecryptfs-utils \
    librolegen \
    python3-core \
    python3-io \
    python3-json \
"

# Built from the webOS-ports fork (webosose master + the former LuneOS patch
# stack merged as commits, plus the audit/hardening work) rather than webosose
# plus patches. Pinned with a plain SRCREV: submission tags are a webosose
# convention and this branch carries none.
SRCREV = "19585a5d43f84ae56bd042fc12de1765be921996"

# Set outright rather than derived from a submission tag. Kept monotonic:
# the patch-stack recipe shipped 1.0.0-48, so anything lower would look
# like a downgrade to opkg on an update.
PV = "1.0.0-49"
PR = "r9"

inherit webos_component
inherit webos_cmake
# The audit/hardening work lives on herrie/fixes, not on the branch
# webos_ports_ose_repo defaults to, so the SRCREV above is not reachable
# from webOS-ports/webOS-OSE. Drop this line once herrie/fixes is merged
# there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_ose_repo
inherit webos_daemon
inherit webos_system_bus

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://luneos-app-permissions \
    file://luneos-app-permissions.json \
"

# Legacy ipks have no "requiredPermissions" in their appinfo.json, which leaves
# them with an empty set of LS2 access control groups. appinstalld runs this
# helper over every application it unpacks, which works out the groups from
# the luna:// calls the application makes and fills them in.
do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/luneos-app-permissions ${D}${bindir}/luneos-app-permissions

    install -d ${D}${sysconfdir}/palm
    install -m 0644 ${UNPACKDIR}/luneos-app-permissions.json ${D}${sysconfdir}/palm/luneos-app-permissions.json
}

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "appinstalld.service"
