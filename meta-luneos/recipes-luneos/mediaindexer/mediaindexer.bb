SUMMARY = "The mediaindexer service component"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS += "db8 glib-2.0 luna-service2 sqlite3 taglib qtbase luna-sysmgr-common"

# We need this in order to have the mime based media detection working
RDEPENDS_${PN} += "shared-mime-info"

PV = "0.1.0-14+git${SRCPV}"
SRCREV = "6b75db0077badb737fe22cf3eb4e0d2f3ce90ae7"

inherit webos_ports_repo
inherit webos_system_bus
inherit cmake_qt5
inherit webos_cmake
inherit webos_filesystem_paths
inherit webos_systemd

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"
S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${webos_sysconfdir}/db/kinds
    cp -rv ${S}/files/db8/kinds/* ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -rv ${S}/files/db8/permissions/* ${D}${webos_sysconfdir}/db/permissions
}

FILES_${PN} += "${webos_sysconfdir}/db/kinds"
