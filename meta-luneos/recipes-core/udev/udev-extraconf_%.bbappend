FILESEXTRAPATHS:prepend := "${THISDIR}/udev-extraconf:"

# Arm a WoWLAN trigger on every Wi-Fi phy so cfg80211 stops disconnecting on
# suspend; see the rule for the measurement behind it.
SRC_URI += "file://80-wifi-wowlan.rules"

do_install:append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/80-wifi-wowlan.rules ${D}${sysconfdir}/udev/rules.d/
}

RDEPENDS:${PN} += "iw"
