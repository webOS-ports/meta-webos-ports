# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

DESCRIPTION = "NFC daemon. Arbitrates access to the NFC hardware and provides \
D-Bus interfaces (org.sailfishos.nfc.*) to NFC aware applications."
LICENSE = "BSD-3-Clause"
SECTION = "webos/connectivity"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c9f20b68b5eba5ff8e714fbacefb9b5f"

# glib-2.0-native provides gdbus-codegen, which generates the D-Bus glue
# file provides libmagic, which tools/ndef-share includes as magic.h
# (upstream nfcd.spec: BuildRequires: file-devel)
DEPENDS = "glib-2.0 glib-2.0-native libglibutil libnfcdef file"

inherit pkgconfig
inherit systemd

SRC_URI = "git://github.com/sailfishos/nfcd.git;protocol=https;branch=master"

PV = "1.2.7"
SRCREV = "f748b1c32dae72dc29e4af276a85bc4bc1d5b000"

# HAVE_DBUSACCESS=0: libdbusaccess is the Sailfish per-application D-Bus access
# control framework, which we don't ship. Access is governed by the installed
# dbus-1 system.d policy instead.
EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}' KEEP_SYMBOLS=1 HAVE_DBUSACCESS=0 LIBDIR=${libdir} UNITDIR=${systemd_unitdir}/system"
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

    # nfcd's dbus_neard plugin exposes a neard compatible org.neard interface
    # and ships the matching D-Bus policy. connman pulls in the real neard,
    # which owns that same path, so the two clash at rootfs assembly:
    #
    #   Package nfcd wants to install file /etc/dbus-1/system.d/org.neard.conf
    #   But that file is already provided by package neard
    #
    # Leave the file to neard and stop nfcd from contending for the bus name.
    rm -f ${D}${sysconfdir}/dbus-1/system.d/org.neard.conf
    sed -i 's|^ExecStart=\(.*\)/nfcd -o syslog|ExecStart=\1/nfcd -o syslog -d dbus_neard|' \
        ${D}${systemd_unitdir}/system/nfcd.service

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

# nfcd starts every adapter in mode 0 (nothing polling): a client still has
# to ask for a mode (webos-nfc-adapter does), and on top of that the adapter
# also has to actually be powered, which needs nfcd's own AlwaysOn setting.
# Real Sailfish devices get that from their (closed) per-device adaptation
# package; on Halium there's no equivalent, so without this nothing ever
# reaches the sensor no matter what asks for a mode. There is no dynamic
# screen-state component to defer to here (checked nfcd's own plugins and
# sailfishos/mce's full module list - neither touches NFC power), so seed it
# statically. Written the same way nfcd persists it itself: 0700 dir, 0600
# file, [Settings] group, see plugins/settings/settings_plugin.c upstream.
# Only on first install - this is nfcd's own mutable state after that, and a
# reinstall/upgrade must not stomp on whatever the user has since set.
pkg_postinst:${PN}() {
    # Runs on the device, where `install` isn't necessarily on PATH (this
    # busybox build doesn't have it) - stick to mkdir/chmod/cat, which are.
    settings_dir="$D${localstatedir}/lib/nfcd"
    settings_file="$settings_dir/settings"
    if [ ! -e "$settings_file" ]; then
        mkdir -p "$settings_dir"
        chmod 0700 "$settings_dir"
        cat > "$settings_file" <<-EOF
			[Settings]
			Enabled=true
			AlwaysOn=true
			EOF
        chmod 0600 "$settings_file"
    fi
}
