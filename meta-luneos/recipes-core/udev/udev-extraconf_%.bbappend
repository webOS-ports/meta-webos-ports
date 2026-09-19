FILESEXTRAPATHS:prepend := "${THISDIR}/udev-extraconf:"

# Arm a WoWLAN trigger on every Wi-Fi phy so cfg80211 stops disconnecting on
# suspend; see the rule for the measurement behind it.
SRC_URI += "file://80-wifi-wowlan.rules"
SRC_URI += "file://luneos-wowlan-arm"

do_install:append() {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/80-wifi-wowlan.rules ${D}${sysconfdir}/udev/rules.d/
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/luneos-wowlan-arm ${D}${sbindir}/
}

RDEPENDS:${PN} += "iw"

# Bumped when the rules shipped from here change, so installed images pick
# up a new rule on upgrade: the base recipe's revision does not move for it.
PR = "r2"
