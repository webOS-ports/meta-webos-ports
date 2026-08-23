# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "NFC daemon. Arbitrates access to the NFC hardware and provides \
D-Bus interfaces (org.sailfishos.nfc.*) to NFC aware applications."
LICENSE = "BSD-3-Clause"
SECTION = "webos/connectivity"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c9f20b68b5eba5ff8e714fbacefb9b5f"

# glib-2.0-native provides gdbus-codegen, which generates the D-Bus glue
DEPENDS = "glib-2.0 glib-2.0-native libglibutil libnfcdef"

inherit pkgconfig
inherit systemd

SRC_URI = "git://github.com/sailfishos/nfcd.git;protocol=https;branch=master"
S = "${WORKDIR}/git"

PV = "1.2.7"
SRCREV = "f748b1c32dae72dc29e4af276a85bc4bc1d5b000"

# HAVE_DBUSACCESS=0: libdbusaccess is the Sailfish per-application D-Bus access
# control framework, which we don't ship. Access is governed by the installed
# dbus-1 system.d policy instead.
EXTRA_OEMAKE = "KEEP_SYMBOLS=1 HAVE_DBUSACCESS=0 LIBDIR=${libdir} UNITDIR=${systemd_unitdir}/system"
PARALLEL_MAKE = ""

do_compile() {
    oe_runmake release pkgconfig
}

do_install() {
    oe_runmake install DESTDIR=${D}

    # Upstream runs nfcd as the dedicated 'nfc' user, which on Sailfish is
    # granted access to the NFC hardware through their own group setup. On
    # Halium the NCI transport is reached over binder, which we don't hand to
    # a non-root user (bluebinder has the same constraint), so run as root.
    sed -i '/^User=nfc$/d' ${D}${systemd_unitdir}/system/nfcd.service

    # Persistent settings (the enabled flag lives here)
    install -d -m 0700 ${D}${localstatedir}/lib/nfcd

    # Adaptation plugins (nfcd-binder-plugin and friends) are loaded from here
    install -d ${D}${libdir}/nfcd/plugins
}

PACKAGES =+ "${PN}-tools"
FILES:${PN}-tools = "${bindir}/*"
FILES:${PN} += " \
    ${systemd_unitdir}/system/nfcd.service \
    ${sysconfdir}/dbus-1/system.d \
    ${localstatedir}/lib/nfcd \
    ${libdir}/nfcd/plugins \
"

RDEPENDS:${PN} += "dbus"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "nfcd.service"

# meta-systemd defaults this to disable, but we want NFC up at boot
SYSTEMD_AUTO_ENABLE:forcevariable = "enable"
