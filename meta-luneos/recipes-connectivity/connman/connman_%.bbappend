FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# we have resolved disabled in systemd
SRC_URI:remove = "file://0001-connman.service-stop-systemd-resolved-when-we-use-co.patch"
# we have networkd disabled in systemd as well
SRC_URI:remove = "file://0001-connman.service-stop-systemd-networkd-when-using-con.patch"

SRC_URI += "file://0001-connman.service.in-start-after-android-system.servic.patch"

RRECOMMENDS:${PN} += "neard connman-vpn connman-plugin-vpn-openvpn connman-plugin-vpn-vpnc connman-plugin-vpn-l2tp connman-plugin-vpn-pppt connman-tests connman-tools connman-wait-online"

# needed for VPN support in ConnMan
PACKAGECONFIG:append = " openvpn vpnc l2tp pptp"

SYSTEMD_SERVICE:${PN}:remove = "connman.service"

do_install:append() {
    rm -vf ${D}${systemd_unitdir}/system/connman.service
}
