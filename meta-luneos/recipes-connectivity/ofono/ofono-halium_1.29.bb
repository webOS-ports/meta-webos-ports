SUMMARY = "open source telephony"
DESCRIPTION = "oFono is a stack for mobile telephony devices on Linux. oFono supports speaking to telephony devices through specific drivers, or with generic AT commands."
HOMEPAGE = "http://www.ofono.org"
BUGTRACKER = "https://01.org/jira/browse/OF"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://src/ofono.h;beginline=1;endline=20;md5=cfa219503f81b681f25290f12cb74bfa"
DEPENDS = "dbus glib-2.0 udev mobile-broadband-provider-info ell"

# Only MACHINEs which start Halium should be using this version
COMPATIBLE_MACHINE = "^halium$"
# This recipe acts as a replacement for the ofono recipe
PROVIDES += "ofono"
RPROVIDES:${PN} += "ofono"
RPROVIDES:${PN}-tests += "ofono-tests"

SRC_URI  = " \
  git://github.com/sailfishos/ofono.git;protocol=https;branch=master \
  file://0002-Add-support-for-the-Ericsson-F5521gw-modem.patch;striplevel=2 \
  file://ofono.service \
"

inherit autotools pkgconfig systemd gobject-introspection-data

SYSTEMD_SERVICE:${PN} = "ofono.service"

SRCREV = "3afa0876c6506f76ef2e45d97cb326c5ff9fef4d"
PV = "1.29+git"

DEPENDS += "dbus-glib libmce-glib"
# For Halium 9 devices we want to use the ofono-binder-plugin, for older devices we might want to use ofono-ril-plugin
RDEPENDS:${PN} += "mobile-broadband-provider-info ofono-conf ofono-binder-plugin"

S = "${WORKDIR}/git/ofono"
# Can't build out of tree right now so we have to build in tree
B = "${S}"

PACKAGECONFIG ??= "\
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez', '', d)} \
"
PACKAGECONFIG[systemd] = "--with-systemdunitdir=${systemd_system_unitdir}/,--with-systemdunitdir="
PACKAGECONFIG[bluez] = "--enable-bluetooth, --disable-bluetooth, bluez5"

EXTRA_OECONF += "--enable-test --disable-datafiles --disable-sailfish-pushforwarder --enable-extra-modems --disable-rilmodem"

do_install:append() {
    # Override default system service configuration
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/ofono.service ${D}${systemd_unitdir}/system/ofono.service

    # Since we use --disable-datafiles we need to install the dbus condif file manually now
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${B}/src/ofono.conf ${D}${sysconfdir}/dbus-1/system.d/
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE:forcevariable = "enable"

PACKAGES =+ "${PN}-tests"

FILES:${PN} += "${systemd_unitdir}"
FILES:${PN}-tests = "${libdir}/ofono/test"

RDEPENDS:${PN} += "dbus"
RDEPENDS:${PN}-tests = "\
    python3 \
    python3-core \
    python3-dbus \
    ${@bb.utils.contains('GI_DATA_ENABLED', 'True', 'python3-pygobject', '', d)} \
"

RRECOMMENDS:${PN} += "kernel-module-tun mobile-broadband-provider-info"
