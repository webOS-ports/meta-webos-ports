# LuneOS host firewall: default-deny inbound rules in place of oe-core's empty
# iptables.rules and ip6tables.rules (iptables.service and ip6tables.service
# were enabled and loaded 0-byte files, so everything was accepted; audit C1).
# The policy and what it lets in are described in the files themselves.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://iptables-fallback.rules \
    file://ip6tables-fallback.rules \
    file://luneos-devmode-firewall \
    file://iptables-devmode.conf \
    file://ip6tables-devmode.conf \
"

do_install:append() {
    # The rules need these; fail here rather than ship rules that fail open.
    grep -q '^:INPUT DROP' ${D}${IPTABLES_RULES_DIR}/iptables.rules || \
        bbfatal "iptables.rules is not LuneOS's - check FILESEXTRAPATHS"

    # Loaded instead when the rules above are refused (no conntrack), so
    # the firewall fails closed; see luneos-devmode-firewall "load".
    install -m 0644 ${UNPACKDIR}/iptables-fallback.rules ${D}${IPTABLES_RULES_DIR}
    install -D -m 0755 ${UNPACKDIR}/luneos-devmode-firewall ${D}${sbindir}/luneos-devmode-firewall
    install -D -m 0644 ${UNPACKDIR}/iptables-devmode.conf \
        ${D}${systemd_system_unitdir}/iptables.service.d/luneos-devmode.conf
    if ${@bb.utils.contains('PACKAGECONFIG', 'ipv6', 'true', 'false', d)}; then
        install -m 0644 ${UNPACKDIR}/ip6tables-fallback.rules ${D}${IPTABLES_RULES_DIR}
        install -D -m 0644 ${UNPACKDIR}/ip6tables-devmode.conf \
            ${D}${systemd_system_unitdir}/ip6tables.service.d/luneos-devmode.conf
    fi
    sed -i -e 's,@SBINDIR@,${sbindir},g' -e 's,@RULESDIR@,${IPTABLES_RULES_DIR},g' \
        ${D}${sbindir}/luneos-devmode-firewall \
        ${D}${systemd_system_unitdir}/ip*tables.service.d/luneos-devmode.conf
    if grep -q "@[A-Z]*@" ${D}${sbindir}/luneos-devmode-firewall ${D}${systemd_system_unitdir}/ip*tables.service.d/luneos-devmode.conf; then
        bbfatal "luneos-devmode-firewall or its drop-ins still have an @VAR@"
    fi
}

# Emulators (qemu, VirtualBox): SSH on the NAT interface eth0 as well, after
# the "# luneos-emulator-ssh" marker in both IPv4 rules files (see the comment
# above it there). systemd_%.bbappend drops 99-default.link on qemuall, so the
# interface is eth0 and not a predictable name.
LUNEOS_EMULATOR_SSH_RULE = "-A INPUT -i eth+ -p tcp --dport 22 -j ACCEPT"

do_install:append:qemuall() {
    for f in iptables.rules iptables-fallback.rules; do
        grep -qx '# luneos-emulator-ssh' ${D}${IPTABLES_RULES_DIR}/$f || \
            bbfatal "$f has no '# luneos-emulator-ssh' marker line"
        sed -i -e '/^# luneos-emulator-ssh$/a ${LUNEOS_EMULATOR_SSH_RULE}' \
            ${D}${IPTABLES_RULES_DIR}/$f
        grep -qxF -- '${LUNEOS_EMULATOR_SSH_RULE}' ${D}${IPTABLES_RULES_DIR}/$f || \
            bbfatal "emulator SSH rule did not land in $f"
    done
}

# The rules then differ per machine on qemu.
PACKAGE_ARCH:qemuall = "${MACHINE_ARCH}"

FILES:${PN} += " \
    ${sbindir}/luneos-devmode-firewall \
    ${systemd_system_unitdir}/iptables.service.d \
    ${systemd_system_unitdir}/ip6tables.service.d \
"

# The matches the rules use. iptables only RRECOMMENDS its modules, and
# without one of them iptables-restore refuses the whole table (and the
# fallback without conntrack needs tcp, udp and icmp too).
RDEPENDS:${PN} += " \
    ${PN}-module-xt-conntrack \
    ${PN}-module-xt-policy \
    ${PN}-module-xt-tcp \
    ${PN}-module-xt-udp \
    ${PN}-module-ipt-icmp \
    ${@bb.utils.contains('PACKAGECONFIG', 'ipv6', '${PN}-module-ip6t-icmp6 ${PN}-module-ip6t-hl', '', d)} \
"

# Bumped when the rules or scripts shipped from here change, so installed
# images pick them up on upgrade: the base recipe's revision does not move
# for it.
PR = "r4"
