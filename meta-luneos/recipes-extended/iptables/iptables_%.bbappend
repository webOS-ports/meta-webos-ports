# LuneOS host firewall: default-deny inbound rules in place of oe-core's empty
# iptables.rules and ip6tables.rules (iptables.service and ip6tables.service
# were enabled and loaded 0-byte files, so everything was accepted; audit C1).
# The policy and what it lets in are described in the files themselves.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://luneos-devmode-firewall \
    file://iptables-devmode.conf \
    file://ip6tables-devmode.conf \
"

do_install:append() {
    # The rules need these; fail here rather than ship rules that fail open.
    grep -q '^:INPUT DROP' ${D}${IPTABLES_RULES_DIR}/iptables.rules || \
        bbfatal "iptables.rules is not LuneOS's - check FILESEXTRAPATHS"

    install -D -m 0755 ${UNPACKDIR}/luneos-devmode-firewall ${D}${sbindir}/luneos-devmode-firewall
    install -D -m 0644 ${UNPACKDIR}/iptables-devmode.conf \
        ${D}${systemd_system_unitdir}/iptables.service.d/luneos-devmode.conf
    if ${@bb.utils.contains('PACKAGECONFIG', 'ipv6', 'true', 'false', d)}; then
        install -D -m 0644 ${UNPACKDIR}/ip6tables-devmode.conf \
            ${D}${systemd_system_unitdir}/ip6tables.service.d/luneos-devmode.conf
    fi
}

FILES:${PN} += " \
    ${sbindir}/luneos-devmode-firewall \
    ${systemd_system_unitdir}/iptables.service.d \
    ${systemd_system_unitdir}/ip6tables.service.d \
"

# The matches the rules use. iptables only RRECOMMENDS its modules, and
# without one of them iptables-restore refuses the whole table, leaving it
# wide open.
RDEPENDS:${PN} += " \
    ${PN}-module-xt-conntrack \
    ${PN}-module-xt-tcp \
    ${PN}-module-xt-udp \
    ${PN}-module-ipt-icmp \
    ${@bb.utils.contains('PACKAGECONFIG', 'ipv6', '${PN}-module-ip6t-icmp6 ${PN}-module-ip6t-hl', '', d)} \
"
