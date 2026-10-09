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

SRCREV = "43b2624d370c425c922c44d78804b1df18772bce"

PV = "3.0.0-103"

PR = "r23"

inherit webos_component
inherit webos_ports_ose_repo
inherit webos_cmake

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

#EXTRA_OECMAKE += "-DWEBOS_QTTESTABILITY_ENABLED:BOOL=${@ '1' if d.getVar('WEBOS_DISTRO_PRERELEASE') != '' else '0'}"

FILES:${PN} += "${base_libdir}"
EXTRA_OECMAKE += "-DCMAKE_POLICY_VERSION_MINIMUM=3.5"

# backup-log.service runs save-journald-logs.py on every shutdown: it dumps the whole boot's journal as
# text into /var/log and tar.gz's /var/log into /var/spool/rdxd/previous_boot_logs.tar.gz for webOS's rdxd
# crash reporter. LuneOS ships no rdxd, so nothing ever reads the result, and the journal is persistent
# where the image keeps it (journalctl -b -1 has the previous boot). On the HP TouchPad it was the
# longest step of a shutdown after normal uptime: 15.9 s, with the random-seed save slowed alongside it.
do_install:append() {
    rm -f ${D}${systemd_system_unitdir}/backup-log.service \
          ${D}${systemd_system_unitdir}/multi-user.target.wants/backup-log.service \
          ${D}${systemd_system_unitdir}/scripts/save-journald-logs.py
}
