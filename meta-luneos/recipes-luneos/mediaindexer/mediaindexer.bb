SUMMARY = "The mediaindexer service component"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS += "db8 glib-2.0 luna-service2 sqlite3 taglib qtbase luna-sysmgr-common"

# We need this in order to have the mime based media detection working
RDEPENDS:${PN} += "shared-mime-info"
# Qt reads an image's width and height through its plugins: qtbase has no
# TIFF or WebP one
RDEPENDS:${PN} += "qtimageformats-plugins"

PV = "0.1.0-14+git"
SRCREV = "7909b927445c5b36abbee36de0b4eb8ff60e0776"
PR = "r6"

inherit webos_ports_repo
inherit webos_system_bus
inherit qt6-cmake
inherit webos_cmake
inherit webos_filesystem_paths
inherit webos_systemd
inherit pkgconfig

LUNEOS_SYSTEMD_SERVICE = "${PN}.service"

SRC_URI = " \
    ${WEBOS_PORTS_GIT_REPO_COMPLETE} \
    file://mediaindexer.service.d/luneos-hardening.conf \
"

do_install:append() {
    install -d ${D}${webos_sysconfdir}/db/kinds
    cp -rv ${S}/files/db8/kinds/* ${D}${webos_sysconfdir}/db/kinds
    install -d ${D}${webos_sysconfdir}/db/permissions
    cp -rv ${S}/files/db8/permissions/* ${D}${webos_sysconfdir}/db/permissions

    # Sandbox for the indexer; see the file.
    install -D -m 0644 ${UNPACKDIR}/mediaindexer.service.d/luneos-hardening.conf \
        ${D}${systemd_system_unitdir}/mediaindexer.service.d/luneos-hardening.conf
}

FILES:${PN} += "${webos_sysconfdir}/db/kinds ${systemd_system_unitdir}/mediaindexer.service.d"

# tenderloin: /media/internal is webOS's FAT partition; see the .mount file.
SRC_URI:append:tenderloin-halium = " \
    file://tenderloin-thumbnails.mount \
    file://tenderloin-thumbnails.conf \
    file://tenderloin-thumbnails-tmpfiles.conf \
"
PACKAGE_ARCH:tenderloin-halium = "${MACHINE_ARCH}"

do_install:append:tenderloin-halium() {
    install -D -m 0644 ${UNPACKDIR}/tenderloin-thumbnails.mount \
        ${D}${systemd_system_unitdir}/media-internal-.thumbnails.mount
    install -D -m 0644 ${UNPACKDIR}/tenderloin-thumbnails.conf \
        ${D}${systemd_system_unitdir}/mediaindexer.service.d/50-tenderloin-thumbnails.conf
    install -D -m 0644 ${UNPACKDIR}/tenderloin-thumbnails-tmpfiles.conf \
        ${D}${nonarch_libdir}/tmpfiles.d/mediaindexer-tenderloin.conf
}

FILES:${PN}:append:tenderloin-halium = " \
    ${systemd_system_unitdir}/mediaindexer.service.d \
    ${systemd_system_unitdir}/media-internal-.thumbnails.mount \
    ${nonarch_libdir}/tmpfiles.d/mediaindexer-tenderloin.conf \
"

CXXFLAGS += "-fpermissive"
