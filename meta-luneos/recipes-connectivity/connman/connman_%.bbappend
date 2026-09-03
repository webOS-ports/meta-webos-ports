FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-connman.service.in-start-after-android-system.servic.patch"

# Extensions webos-connman-adapter relies on. These replace the webOS OSE
# connman fork patches we used to carry on connman 1.42; the P2P/WiFi Direct
# half of that fork is deliberately not ported, LuneOS does not use it.
SRC_URI += " \
    file://0001-technology-expose-the-list-of-interfaces-per-technol.patch \
    file://0002-technology-add-TetheringIPAddress-and-TetheringChann.patch \
    file://0003-technology-add-StartWPS-and-CancelWPS.patch \
"

# neard is not listed here: PACKAGECONFIG[nfc] below already pulls it in as a
# hard RDEPENDS, so recommending it separately would be redundant.
RRECOMMENDS:${PN} += "connman-vpn connman-plugin-vpn-openvpn connman-plugin-vpn-vpnc connman-plugin-vpn-l2tp connman-plugin-vpn-pptp connman-plugin-vpn-wireguard connman-tests connman-tools connman-wait-online"

# needed for VPN support in ConnMan. wireguard is the one modern transport
# upstream supports and it only needs libmnl, so there is no reason to ship
# the other four without it.
PACKAGECONFIG:append = " openvpn vpnc l2tp pptp wireguard"

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
