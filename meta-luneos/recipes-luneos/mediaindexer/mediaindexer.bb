SUMMARY = "The mediaindexer service component"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS += "db8 glib-2.0 luna-service2 sqlite3 taglib qtbase luna-sysmgr-common"

# We need this in order to have the mime based media detection working
RDEPENDS:${PN} += "shared-mime-info"

PV = "0.1.0-14+git${SRCPV}"
SRCREV = "f9784494312c961c25b870f7fc7083492ea9f0e1"

inherit webos_ports_repo
inherit webos_system_bus
inherit qt6-cmake
inherit webos_cmake
inherit webos_filesystem_paths
inherit webos_systemd
inherit pkgconfig

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://0001-Fix-build-with-gcc-13.patch \
"
S = "${WORKDIR}/git"

do_install:append() {
    install -d ${D}${webos_sysconfdir}/db/kinds
    cp -rv ${S}/files/db8/kinds/* ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -rv ${S}/files/db8/permissions/* ${D}${webos_sysconfdir}/db/permissions
}

FILES:${PN} += "${webos_sysconfdir}/db/kinds"

CXXFLAGS += "-fpermissive"
