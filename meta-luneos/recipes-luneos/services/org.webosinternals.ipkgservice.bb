SUMMARY = "Package management service for Open webOS"
SECTION = "webos/services"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS = "luna-service2 glib-2.0 json-c"

PV = "2.0.0-2+git${SRCPV}"
SRCREV = "4809012bdc4661f246d97e75af9b90d4bd0a83c3"

inherit webos_ports_repo
inherit webos_cmake
inherit pkgconfig
inherit webos_system_bus

WEBOS_REPO_NAME = "preware"
SRC_URI = "${WEBOS_PORTS_GIT_REPO_COMPLETE}"

S = "${WORKDIR}/git/oe-service"

do_install_append() {
    APPS=/media/cryptofs/apps

    # NOTE: We can safely create everything here in ${localstatedir} as those
    # things will be copied on first boot to their right location.

    # Create the opkg config and database areas
    mkdir -p ${D}${APPS}/${sysconfdir}/opkg
    mkdir -p ${D}${APPS}/${localstatedir}/lib/opkg/cache

    # We provide an empty status file to satisfy the ipkgservice
    touch ${D}${APPS}/${localstatedir}/lib/opkg/status

    # Remove all list database cache files
    rm -f ${D}${APPS}/${localstatedir}/lib/opkg/lists/*

    # Set up the architecture configuration file
    rm -f ${D}${APPS}/${sysconfdir}/opkg/arch.conf
    ln -sf ${sysconfdir}/opkg/arch.conf ${D}${APPS}/${sysconfdir}/opkg/arch.conf

    # Install webosports all-arch feeds
    echo "src/gz webosports http://feeds.webos-ports.org/webos-ports/all" > ${D}${APPS}/${sysconfdir}/opkg/webos-ports.conf

    # Add additional feeds which are disabled by default and NOT SUPPORTED by webOS
    # ports / LuneOS. The user has to turn them on manually to use them.
    echo "src Macaw-enyo http://minego.net/preware/macaw-enyo" > ${D}${APPS}/${sysconfdir}/opkg/macaw-enyo.conf.disabled
    echo "src Hominid-Software http://hominidsoftware.com/preware" > ${D}${APPS}/${sysconfdir}/opkg/hominid-software.conf.disabled
    echo "src/gz FeedSpider2 http://feedspider.net/luneos" > ${D}${APPS}/${sysconfdir}/opkg/feedspider.conf.disabled
}

FILES_${PN} += "/media/cryptofs/apps"
