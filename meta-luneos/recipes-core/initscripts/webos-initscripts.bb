# Copyright (c) 2014-2024 LG Electronics, Inc.

SUMMARY = "Systemd service files for system services"
AUTHOR = "Sukil Hong <sukil.hong@lge.com>"
SECTION = "webos/base"
LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = " \
    file://LICENSE;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://oss-pkg-info.yaml;md5=2bdfe040dcf81b4038370ae96036c519 \
"

# TODO: systemd dependency is for fake initctl.
# The dependency needs to be deleted after deleting fake initctl.
DEPENDS = "systemd"

VIRTUAL-RUNTIME_bash ?= "bash"
RDEPENDS:${PN} = "${VIRTUAL-RUNTIME_init_manager} ${VIRTUAL-RUNTIME_bash} python3"

PROVIDES = "initscripts"
RPROVIDES:${PN} = "initscripts initd-functions"

# Built from the webOS-ports fork (webosose plus the LuneOS changes merged as
# commits) rather than webosose plus a patch stack: the tmpfiles/zram/
# backup-log patches that used to live in this directory are commits there
# now, together with the webos-dis.target.wants retarget for the renamed
# luna-sysservice.service unit (a rename patch(1) could not express, since it
# cannot create symlinks). Pinned with a plain SRCREV - submission tags are a
# webosose convention and this branch carries none. The previous LuneOS line
# of this fork is preserved as old/webOS-ports/webOS-OSE.
SRCREV = "00e94529e71473002228b29f6bc91ceb89a6ec38"

# Set outright rather than derived from a submission tag via WEBOS_VERSION.
# Kept monotonic: the patch-stack recipe shipped 3.0.0-102, so anything lower
# would look like a downgrade to opkg on an update.
PV = "3.0.0-103"

PR = "r20"

inherit webos_component
inherit webos_ports_ose_repo
inherit webos_cmake

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

#EXTRA_OECMAKE += "-DWEBOS_QTTESTABILITY_ENABLED:BOOL=${@ '1' if d.getVar('WEBOS_DISTRO_PRERELEASE') != '' else '0'}"

FILES:${PN} += "${base_libdir}"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"
