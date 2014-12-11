SUMMARY = "The mediaindexer service component"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS += "db8 glib-2.0 luna-service2 sqlite3 taglib qtbase luna-sysmgr-common"

# We need this in order to have the mime based media detection working
RDEPENDS_${PN} += "shared-mime-info"

WEBOS_VERSION = "0.1.0-14_eb7bcf346cda0671608a96c037aee3797ad8dc79"

inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus
inherit cmake_qt5
inherit webos_cmake
inherit webos_systemd

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${webos_sysconfdir}/db/kinds
    cp -rv ${S}/files/db8/kinds/* ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -rv ${S}/files/db8/permissions/* ${D}${webos_sysconfdir}/db/permissions
}

FILES_${PN} += "${webos_sysconfdir}/db/kinds"
