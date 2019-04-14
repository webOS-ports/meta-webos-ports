FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

# we have resolved disabled in systemd
SRC_URI_remove = "file://0001-connman.service-stop-systemd-resolved-when-we-use-co.patch"

SRC_URI += "file://0001-connman.service.in-start-after-android-system.servic.patch"

PACKAGECONFIG[nfc] = "--enable-neard, --disable-neard, neard, neard"
RRECOMMENDS_${PN} += "neard connman-vpn connman-plugin-vpn-openvpn connman-plugin-vpn-vpnc connman-plugin-vpn-l2tp connman-plugin-vpn-pppt connman-tests connman-tools connman-wait-online"

# needed for VPN support in ConnMan
PACKAGECONFIG_append = " openvpn vpnc l2tp pptp"
