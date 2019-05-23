SUMMARY = "The Geolocation Service"
DESCRIPTION = "Geoclue is a D-Bus service that provides location information. \
The primary goal of the Geoclue project is to make creating location-aware applications \
as simple as possible, while the secondary goal is to ensure that no application \
can access location information without explicit permission from user."
LICENSE = "GPLv2.0+"
SECTION = "console/network"

LIC_FILES_CHKSUM = "file://COPYING;md5=bdfdd4986a0853eb84eeba85f9d0c4d6"

DEPENDS = "glib-2.0 dbus json-glib libsoup-2.4 intltool-native"

inherit meson pkgconfig gtk-doc gobject-introspection vala

SRC_URI = " \
    https://gitlab.freedesktop.org/geoclue/geoclue/-/archive/${PV}/geoclue-${PV}.tar \
"

SRC_URI[md5sum] = "6228301ed62b587ebaa0438b97ce66e4"
SRC_URI[sha256sum] = "3caa5e10190a34c17a9b3a9d1d8a19797ed197f07f36f6e35ce533a379efe155"

export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

# Without this line, package is declared a library and named libgeoclue*
AUTO_LIBNAME_PKGS = ""

PACKAGECONFIG ??= "3g modem-gps cdma nmea lib"
PACKAGECONFIG[3g] = "-D3g-source=true,-D3g-source=false,modemmanager"
PACKAGECONFIG[modem-gps] = "-Dmodem-gps-source=true,-Dmodem-gps-source=false,modemmanager"
PACKAGECONFIG[cdma] = "-Dcdma-source=true,-Dcdma-source=false,modemmanager"
PACKAGECONFIG[nmea] = "-Dnmea-source=true,-Dnmea-source=false,avahi"
PACKAGECONFIG[lib] = "-Dlibgeoclue=true,-Dlibgeoclue=false,gobject-introspection"

GTKDOC_MESON_OPTION = "gtk-doc"

# backport gtk-doc.bbclass changes from Yocto 2.8 Zeus to make GTKDOC_MESON_OPTION work in Yocto 2.7 Warrior
# http://git.openembedded.org/openembedded-core/commit/meta/classes/gtk-doc.bbclass?id=ff578f4451a0a199202e576b647840910b4d3f59
# meson: default option name to enable/disable gtk-doc. This matches most
# project's configuration. In doubts - check meson_options.txt in project's
# source path.
GTKDOC_MESON_OPTION ?= 'docs'
GTKDOC_MESON_ENABLE_FLAG ?= 'true'
GTKDOC_MESON_DISABLE_FLAG ?= 'false'
EXTRA_OEMESON_prepend_class-target = "-D${GTKDOC_MESON_OPTION}=${@bb.utils.contains('GTKDOC_ENABLED', 'True', '${GTKDOC_MESON_ENABLE_FLAG}', '${GTKDOC_MESON_DISABLE_FLAG}', d)} "
EXTRA_OEMESON_prepend_class-native = "-D${GTKDOC_MESON_OPTION}=${GTKDOC_MESON_DISABLE_FLAG} "
EXTRA_OEMESON_prepend_class-nativesdk = "-D${GTKDOC_MESON_OPTION}=${GTKDOC_MESON_DISABLE_FLAG} "

EXTRA_OEMESON += " \
    -Ddbus-sys-dir=${sysconfdir}/dbus-1/system.d \
    -Ddemo-agent=false \
"

FILES_${PN} += " \
    ${datadir}/dbus-1/system-services \
    ${libdir} \
    ${systemd_unitdir} \
    ${prefix}/libexec \
"

FILES_${PN}-dev += " \
    ${datadir}/dbus-1/interfaces \
    ${datadir}/gir-1.0 \
"
