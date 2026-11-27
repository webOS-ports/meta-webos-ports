# Copyright (c) 2026 Herman van Hazendonk <github.com@herrie.org>

SUMMARY = "Stand-in Android netd with a DNS proxy for vendor processes"
DESCRIPTION = "dummy_netd registers a do-nothing android.system.net.netd INetd, \
for vendor services that wait for netd, and answers bionic's getaddrinfo() on \
/dev/socket/dnsproxyd with the host resolver. LuneOS runs no netd, so without \
it no vendor daemon can resolve a name: on MediaTek devices epdg_wod never \
finds the ePDG and VoWiFi never registers."
HOMEPAGE = "https://github.com/mer-hybris/dummy_netd"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b19557c3df1111718d1b6d3542dcfb3d"
SECTION = "webos/support"

DEPENDS = "glib-2.0 libgbinder libglibutil systemd"

inherit pkgconfig systemd

# Upstream master, ea3e678 being the merge of the DNS proxy (mer-hybris PR #9).
# The patches are from the fork at github.com/Herrie82/dummy_netd. They are
# not pushed or submitted yet, and the fork also has the host tests
# (tests/), which are not carried here because nothing in them is built or
# installed.
SRC_URI = "git://github.com/mer-hybris/dummy_netd.git;branch=master;protocol=https \
    file://0001-dummy_netd-Let-the-build-choose-where-the-DNS-proxy-.patch \
    file://0002-dummy_netd-Refuse-getaddrinfo-hints-that-are-not-num.patch \
    file://0003-dummy_netd-Report-DNS-proxy-errors-with-g_strerror.patch \
    file://0004-dummy_netd-Create-the-DNS-proxy-socket-with-its-mode.patch \
    file://0005-dummy_netd-Forget-the-DNS-listener-source-once-it-is.patch \
    file://0006-dummy_netd-Walk-getaddrinfo-results-through-a-const-.patch \
    file://0007-dummy_netd-Declare-the-DNS-proxy-in-a-header.patch \
"

PV = "1.0.4+git"
SRCREV = "ea3e678ccf03328a09580b111b97d611920a19e6"

# The Makefile sets CC itself, so it has to come in on the command line.
# KEEP_SYMBOLS=1 leaves stripping to the packaging.
EXTRA_OEMAKE = "KEEP_SYMBOLS=1 CC='${CC}' UNITDIR=${systemd_system_unitdir}"

do_compile() {
    oe_runmake release
}

do_install() {
    oe_runmake install DESTDIR=${D}

    # Upstream orders the unit after droid-hal-init.service, Sailfish's
    # Android container. Ours is android-system.service, which also creates
    # /dev/socket and the inet group (gid 3003) the unit runs under.
    sed -i 's/^After=droid-hal-init\.service$/After=android-system.service/' \
        ${D}${systemd_system_unitdir}/dummy_netd.service
    # LuneOS boots multi-user.target, so WantedBy=graphical.target would
    # leave the unit disabled.
    sed -i 's/^WantedBy=graphical\.target$/WantedBy=multi-user.target/' \
        ${D}${systemd_system_unitdir}/dummy_netd.service
    grep -qx 'After=android-system.service' ${D}${systemd_system_unitdir}/dummy_netd.service
    grep -qx 'WantedBy=multi-user.target' ${D}${systemd_system_unitdir}/dummy_netd.service
    grep -qx 'Before=ofono.service' ${D}${systemd_system_unitdir}/dummy_netd.service
    grep -qx 'Group=inet' ${D}${systemd_system_unitdir}/dummy_netd.service
}

SYSTEMD_SERVICE:${PN} = "dummy_netd.service"
