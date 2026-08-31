# Copyright (c) 2019 Christophe Chapuis <chris.chapuis@gmail.com>

DESCRIPTION = "Simple proxy for using android binder based bluetooth through vhci."
LICENSE = "GPL-2.0-or-later"
SECTION = "webos/support"
LIC_FILES_CHKSUM = "file://bluebinder.c;beginline=1;endline=27;md5=ba7fb591c7626c434dcc691ce7797fd1"

DEPENDS = "libgbinder glib-2.0 libglibutil bluez5 systemd"
RDEPENDS:${PN} = "android-property-service"

# Rdepends on android-property-service which depends on libhybris which has this restriction
COMPATIBLE_MACHINE = "^halium$"

# Rdepends on android-property-service which is MACHINE_ARCH
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit pkgconfig
inherit systemd

# 0001-Use-CC-as-compiler.patch was dropped at 1.0.20: upstream's Makefile now has
# "CC ?= $(CROSS_COMPILE)gcc" (8398480), so the environment CC is already honored.
SRC_URI = "git://github.com/mer-hybris/bluebinder.git;branch=master;protocol=https \
           file://0003-bluebinder-set-under-reported-supported-commands.patch \
"
SRC_URI:append:tissot-halium = " file://0002-service-load-after-wifi-module-load.patch"

PV = "1.0.20"
SRCREV = "c3e1b155e308f6df9c9a02dbd909a44e7319ab7d"

CFLAGS += "--sysroot=${RECIPE_SYSROOT} ${LDFLAGS}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "bluebinder.service"

do_install() {
    make install DESTDIR=${D}

    install -d ${D}${sbindir}
    install -m 0755 ${S}/bluebinder_post.sh ${D}${sbindir}/bluebinder_post.sh
    install -m 0755 ${S}/bluebinder_wait.sh ${D}${sbindir}/bluebinder_wait.sh

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/bluebinder.service ${D}${systemd_unitdir}/system/

    # Upstream's unit is written for Sailfish, and four things in it stop the
    # service ever reaching "active" here. Without them there is no hci0 at all,
    # so no Bluetooth.
    #
    # 1. It runs the two helper scripts out of /usr/bin/droid, which is a
    #    droid-hal path that does not exist on LuneOS - do_install above puts
    #    them in ${sbindir}. Left alone, ExecStartPre fails and the service
    #    never starts.
    # 2. DevicePolicy=strict permits only the devices DeviceAllow lists, and
    #    /dev/null is not among them. systemd cannot even set up stdin:
    #        bluebinder.service: Failed to set up standard input:
    #            Operation not permitted
    #        Control process exited, code=exited, status=208/STDIN
    #    "closed" keeps the same restriction but grants the usual /dev/null,
    #    zero, full, random and urandom. This only began to bite when the
    #    machine moved to cgroup v1, where the devices controller enforces it.
    # 3. RestrictAddressFamilies=AF_BLUETOOTH seccomp-filters socket(2) down to
    #    Bluetooth only - and sd_notify() needs AF_UNIX. bluebinder does support
    #    readiness notification (it links libsystemd and calls sd_notify), but
    #    the call was being blocked silently, so a Type=notify unit sat in
    #    "activating" until TimeoutStartSec, then Restart=always tore down the
    #    vhci it had just built:
    #        Successfully initialized vhci bluetooth
    #        Writing packet from HAL to vhci device failed: No such device
    # 4. It orders itself after droid-hal-init.service, which is Sailfish's
    #    container init. Ours is android-system.service.
    # 5. It installs into graphical.target, which LuneOS never reaches -
    #    systemctl get-default is multi-user.target - so even with all of the
    #    above fixed the unit is simply never pulled in and sits inactive.
    sed -i \
        -e 's|/usr/bin/droid/|${sbindir}/|g' \
        -e 's|^DevicePolicy=strict$|DevicePolicy=closed|' \
        -e 's|^RestrictAddressFamilies=AF_BLUETOOTH$|RestrictAddressFamilies=AF_UNIX AF_NETLINK AF_BLUETOOTH|' \
        -e 's|^After=droid-hal-init.service$|After=android-system.service|' \
        -e 's|^WantedBy=graphical.target$|WantedBy=multi-user.target|' \
        ${D}${systemd_unitdir}/system/bluebinder.service
}

FILES:${PN} += "${sbindir}/bluebinder_post.sh ${sbindir}/bluebinder_wait.sh"
