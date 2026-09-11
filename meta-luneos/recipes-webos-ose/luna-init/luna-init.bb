# Copyright (c) 2012-2025 LG Electronics, Inc.

SUMMARY = "Initialization, setup, and font files used by luna-sysmgr and luna-sysservice"
AUTHOR = "Yogish S <yogish.s@lge.com>"
SECTION = "webos/base"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

DEPENDS = "tzdata python3-pytz-native"

# Built from the webOS-ports fork (webosose master + LuneOS customizations
# merged as commits) rather than webosose plus a patch stack. Pinned with a
# plain SRCREV: submission tags are a webosose convention and this branch
# carries none. The branch itself comes from webos_ports_ose_repo below.
SRCREV = "51eee76447e60e17f38e3e89e1f7d6d9b49b032f"

PR = "r21"

inherit webos_arch_indep
# The cleanup and hardening work (webOS-ports/luna-init#1) lives on
# herrie/fixes, not on the branch webos_ports_ose_repo defaults to, so the
# SRCREV above is not reachable from webOS-ports/webOS-OSE. Drop this line
# once herrie/fixes is merged there.
WEBOS_GIT_PARAM_BRANCH = "herrie/fixes"
inherit webos_ports_ose_repo
inherit webos_cmake
inherit python3native

# Set outright rather than derived from a submission tag. Kept monotonic: the
# patch-stack recipe shipped 2.0.1-11, so anything lower would look like a
# downgrade to opkg on an update.
PV = "2.0.1-12"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install:append() {
    # Expand fonts tarball
    if [ -e ${S}/files/conf/fonts/fonts.tgz ]; then
        install -d ${D}${datadir}/fonts
        tar xvzf ${S}/files/conf/fonts/fonts.tgz --directory=${D}${datadir}/fonts
        chown -R root:root ${D}${datadir}/fonts
    fi
    install -d ${D}${webos_sysconfdir}
    install -v -m 644 ${S}/files/conf/locale.txt ${D}${webos_sysconfdir}
}

PACKAGES =+ "${PN}-fonts"
FILES:${PN} += "${webos_prefix} ${webos_sysconfdir}"
# Below is needed for the LuneOS additions
FILES:${PN} += "${webos_sysmgr_datadir}/customization/"
FILES:${PN}-fonts += "${datadir}/fonts/"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
