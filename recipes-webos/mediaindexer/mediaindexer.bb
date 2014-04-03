SUMMARY = "The mediaindexer service component"
LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS += "db8 glib-2.0 luna-service2 sqlite3 taglib"

# We need this in order to have the mime based media detection working
RDEPENDS_${PN} += "shared-mime-info"

WEBOS_VERSION = "0.1.0-5_2c55f6be842d9e5626db51e4e1e61e4b0d9ec8bc"

inherit webos_component
inherit webos_public_repo
inherit webos_enhanced_submissions
inherit webos_system_bus
inherit webos_cmake
inherit webos_systemd

SRC_URI = "git://github.com/webOS-ports/${PN};protocol=git;branch=master"
S = "${WORKDIR}/git"

do_install_append() {
    install -d ${D}${webos_sysconfdir}/db/kinds
    cp -rv ${S}/files/db8/kinds/* ${D}${webos_sysconfdir}/db/kinds
}

FILES_${PN} += "${webos_sysconfdir}/db/kinds"
