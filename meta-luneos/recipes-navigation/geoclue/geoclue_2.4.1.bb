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
    http://www.freedesktop.org/software/geoclue/releases/2.4/geoclue-${PV}.tar.xz \
"

SRC_URI[md5sum] = "3b4ccf1ce72cebd6becacedb20f52845"
SRC_URI[sha256sum] = "9a19fd00f6064d6f29e791ee28afb839431e280fed3ad851aa04a0ddae0d34d4"

EXTRA_OECONF += " \
    --with-dbus-service-user=root \
    --with-dbus-sys-dir=${sysconfdir}/dbus-1/system.d \
    --with-systemdsystemunitdir=${systemd_unitdir}/system \
"

PACKAGECONFIG ??= ""
PACKAGECONFIG[3g-source] = "--enable-3g-source,--disable-3g-source,networkmanager"
PACKAGECONFIG[cdma-source] = "--enable-cdma-source,--disable-cdma-source,networkmanager"
PACKAGECONFIG[modem-gps-source] = "--enable-modem-gps-source,--disable-modem-gps-source,networkmanager"

PACKAGES += "${PN}-examples"

FILES_${PN}-examples = "$[libdir}/geoclue/geoclue-2.0/demos"
FILES_${PN} += " \
    ${datadir}/dbus-1/system-services/org.freedesktop.GeoClue2.service \
    ${datadir}/geoclue-2.0/geoclue-interface.xml \
"
FILES_${PN}-dev += "${datadir}/dbus-1/interfaces"
FILES_${PN}-dbg += "${libdir}/geoclue/geoclue-2.0/demos/.debug"

#AVAHI is used for NMEA sources introduced in GeoClue 2.3.0
#For iio-sensor-proxy first need to see if it is of any use for our targets before including it as well in RRECOMMENDS. It would need its own recipe as well first too since it's not in OE yet.
RRECOMMENDS_${PN} = "avahi-daemon"

RDEPENDS_${PN} = " \
  libavahi-common \
  libavahi-client \
  libavahi-glib \
"
