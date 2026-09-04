FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-connman.service.in-start-after-android-system.servic.patch"

# Extensions webos-connman-adapter relies on. These replace the webOS OSE
# connman fork patches we used to carry on connman 1.42; the P2P/WiFi Direct
# half of that fork is deliberately not ported, LuneOS does not use it.
SRC_URI += " \
    file://0001-technology-expose-the-list-of-interfaces-per-technol.patch \
    file://0002-technology-add-TetheringIPAddress-and-TetheringChann.patch \
    file://0003-technology-add-StartWPS-and-CancelWPS.patch \
    file://0004-vpn-provider-skip-connections-with-no-object-path-i.patch \
    file://connman-vpn.service.d/luneos-caps.conf \
"

# See connman-vpn.service.d/luneos-caps.conf for why: upstream's
# CapabilityBoundingSet on connman-vpn.service is missing CAP_SYS_ADMIN and
# CAP_DAC_OVERRIDE, which the l2tp plugin's client-side pppd needs to open
# /dev/ppp for its kernel PPP unit. Dropped in rather than patched so it
# survives a connman version bump untouched.
FILES:${PN}-vpn += "${systemd_system_unitdir}/connman-vpn.service.d/luneos-caps.conf"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}/connman-vpn.service.d
    install -m 0644 ${UNPACKDIR}/connman-vpn.service.d/luneos-caps.conf \
        ${D}${systemd_system_unitdir}/connman-vpn.service.d/luneos-caps.conf
}

# neard is not listed here: PACKAGECONFIG[nfc] below already pulls it in as a
# hard RDEPENDS, so recommending it separately would be redundant.
RRECOMMENDS:${PN} += "connman-vpn connman-plugin-vpn-openvpn connman-plugin-vpn-vpnc connman-plugin-vpn-openconnect connman-plugin-vpn-l2tp connman-plugin-vpn-pptp connman-plugin-vpn-wireguard connman-tests connman-tools connman-wait-online"

# needed for VPN support in ConnMan. wireguard is the one modern transport
# upstream supports and it only needs libmnl, so there is no reason to ship
# the others without it.
PACKAGECONFIG:append = " openvpn vpnc l2tp pptp wireguard"

# openconnect is the modern replacement for the Cisco AnyConnect agent legacy
# webOS shipped, and it also covers Juniper/Pulse, PAN GlobalProtect and
# Fortinet SSL VPN. connman 2.0 supports it fully (vpn/plugins/openconnect.c),
# but oe-core's connman.inc has no PACKAGECONFIG[openconnect] entry at all - it
# only ever documented "openvpn vpnc l2tp pptp" - so the knob has to be declared
# here before it can be switched on.
#
# Note the third field (build DEPENDS) is populated here, unlike the openvpn,
# vpnc, l2tp and pptp entries in connman.inc which leave it empty. Those plugins
# only exec a client binary, so a runtime dependency is enough. openconnect is
# different: configure does PKG_CHECK_MODULES(LIBOPENCONNECT, openconnect >= 8)
# and the plugin links @LIBOPENCONNECT_LIBS@, so the headers and .pc file have to
# be in the sysroot at build time. --with-openconnect is passed explicitly so the
# AC_PATH_PROG probe does not run and fail under cross-compile. openconnect 9.12
# in meta-networking ships the library and the binary in one package, so the same
# name serves as both the build and the runtime dependency.
PACKAGECONFIG[openconnect] = "--enable-openconnect --with-openconnect=${sbindir}/openconnect,--disable-openconnect,openconnect,openconnect"
PACKAGECONFIG:append = " openconnect"

# The connman-plugin-vpn-openconnect package itself is created automatically:
# populate_packages:prepend() in connman.inc runs do_split_packages() over
# ${libdir}/connman/plugins-vpn/, so every .so there gets its own package with
# FILES and INSANE_SKIP already set. Only the runtime deps need stating.
SUMMARY:${PN}-plugin-vpn-openconnect = "An OpenConnect plugin for ConnMan VPN"
RDEPENDS:${PN}-plugin-vpn-openconnect += "${PN}-vpn openconnect"

# ${libdir}/connman/scripts/vpn-script is built for *either* the openconnect or
# the vpnc plugin - Makefile.plugins has "if OPENCONNECT ... else if VPNC" - and
# both plugins invoke it (openconnect.c:1107 passes --script SCRIPTDIR/vpn-script).
# oe-core packages it only under ${PN}-plugin-vpn-vpnc, which would leave
# ${PN}-plugin-vpn-openconnect silently broken on any image that installs
# openconnect without vpnc. Move it into ${PN}-vpn, which both plugins already
# RDEPEND on. Worth sending upstream.
FILES:${PN}-plugin-vpn-vpnc:remove = "${libdir}/connman/scripts/vpn-script"
FILES:${PN}-vpn += "${libdir}/connman/scripts/vpn-script"

# While here: oe-core also lists ${libdir}/connman/scripts/openconnect-script in
# FILES:${PN}-plugin-vpn-vpnc. connman 2.0 does not build that file any more.
# Harmless (a non-existent path in FILES is ignored), noted so the next person
# does not go looking for it.

# nfc is in DISTRO_FEATURES but oe-core does not map it onto connman the way
# it maps bluetooth onto bluez, so connman was being built --disable-neard
# while we separately tried to recommend neard into the image. Enable the
# plugin so the two agree.
PACKAGECONFIG:append = " ${@bb.utils.filter('DISTRO_FEATURES', 'nfc', d)}"

SYSTEMD_SERVICE:${PN}:remove = "connman.service"

do_install:append() {
    rm -vf ${D}${systemd_unitdir}/system/connman.service
}

# neard drives the in-kernel Linux NFC subsystem, which Halium devices don't
# have: there the NFC controller is reached through the Android HAL by nfcd.
# Keep neard for mainline targets, but don't pull it in on Halium.
RRECOMMENDS:${PN}:remove:halium = "neard"
