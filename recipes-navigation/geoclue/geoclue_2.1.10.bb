SUMMARY = "The Geolocation Service"
DESCRIPTION = "Geoclue is a D-Bus service that provides location information. \
The primary goal of the Geoclue project is to make creating location-aware applications \
as simple as possible, while the secondary goal is to ensure that no application \
can access location information without explicit permission from user."
LICENSE = "GPLv2.0+"
SECTION = "console/network"

LIC_FILES_CHKSUM = "file://COPYING;md5=8114b83a0435d8136b47bd70111ce5cd"

DEPENDS = "glib-2.0 dbus dbus-glib json-glib libsoup-2.4"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "${PN}.service"

inherit autotools pkgconfig gtk-doc systemd

SRC_URI = " \
    http://www.freedesktop.org/software/geoclue/releases/2.1/geoclue-${PV}.tar.xz \
"

SRC_URI[md5sum] = "aaa6c7a2a48a8fa74838345722d80e9f"
SRC_URI[sha256sum] = "f1d7e7a9c60633d3d747f55fee848299114b3222a4d65e955c2cbcd297d01c68"

EXTRA_OECONF += " \
    --with-dbus-service-user=root \
    --with-dbus-sys-dir=${sysconfdir}/dbus-1/system.d \
"

PACKAGECONFIG ??= ""
PACKAGECONFIG[3g-source] = "--enable-3g-source,--disable-3g-source,networkmanager"
PACKAGECONFIG[cdma-sozrce] = "--enable-cdma-source,--disable-cdma-source,networkmanager"
PACKAGECONFIG[modem-gps-source] = "--enable-modem-gps-source,--disable-modem-gps-source,networkmanager"

PACKAGES += "${PN}-examples"

FILES_${PN}-examples = "$[libdir}/geoclue/geoclue-2.0/demos"
FILES_${PN} += " \
    ${datadir}/dbus-1/system-services/org.freedesktop.GeoClue2.service \
    ${datadir}/geoclue-2.0/geoclue-interface.xml \
"
FILES_${PN}-dev += "${datadir}/dbus-1/interfaces"
FILES_${PN}-dbg += "${libdir}/geoclue/geoclue-2.0/demos/.debug"
