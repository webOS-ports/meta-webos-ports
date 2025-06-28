SUMMARY = "Tasks App for LuneOS"
SECTION = "webos/apps"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit webos_ports_repo
inherit allarch
inherit webos_filesystem_paths

SRCREV = "5400f2f4eac6da564759f7491b280791892de087"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

do_install() {

    # db8 kinds and permissions
    install -d ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -vrf ${S}/configuration/db/kinds/* ${D}${webos_sysconfdir}/db/kinds/
    cp -vrf ${S}/configuration/db/permissions/* ${D}${webos_sysconfdir}/db/permissions/

}

