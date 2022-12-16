FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# we have resolved disabled in systemd
SRC_URI:remove = "file://0001-connman.service-stop-systemd-resolved-when-we-use-co.patch"
# we have networkd disabled in systemd as well
SRC_URI:remove = "file://0001-connman.service-stop-systemd-networkd-when-using-con.patch"

SRC_URI += "file://0001-connman.service.in-start-after-android-system.servic.patch"

#Patches from connman-webos from webOS OSE
SRC_URI += " \
    file://0001-Add-Changes-for-p2p-setState-api.patch \
    file://0002-Add-Changes-for-p2p-getDeviceName-api.patch \
    file://0003-Add-Changes-for-p2p-addService-setwifidisplayinfo-fi.patch \
    file://0004-Add-Changes-for-tethering-setState-api.patch \
    file://0005-Add-Changes-for-p2p-create-group-api.patch \
    file://0006-Add-Changes-for-No-Internet-issue-connman.patch \
    file://0007-Fix-for-create-group-api-and-crash-issue.patch \
    file://0008-Revert-Fix-for-create-group-api-and-crash-issue.patch \
    file://0009-Fix-for-Create-Group-api-and-crash-issue.patch \
    file://0010-Fix-for-peer-Device-List-Info-issue.patch \
"


RRECOMMENDS:${PN} += "neard connman-vpn connman-plugin-vpn-openvpn connman-plugin-vpn-vpnc connman-plugin-vpn-l2tp connman-plugin-vpn-pppt connman-tests connman-tools connman-wait-online"

# needed for VPN support in ConnMan
PACKAGECONFIG:append = " openvpn vpnc l2tp pptp"
