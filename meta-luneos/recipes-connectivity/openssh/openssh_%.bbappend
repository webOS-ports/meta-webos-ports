# Ship our own sshd_check_keys, restoring the host-key generation fallback that
# oe-core dd27f9d869 removed; the reasoning is in the script. It arrives by
# file:// SRC_URI so it never reaches ${S} and cannot be patched - override it
# instead. Re-diff against oe-core's copy when updating.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# sshd only in developer mode: a drop-in for sshd.socket (see the file).
SRC_URI += "file://sshd-devmode.conf"

do_install:append() {
    if ${@bb.utils.contains('PACKAGECONFIG','systemd-sshd-socket-mode','true','false',d)}; then
        install -d ${D}${systemd_system_unitdir}/sshd.socket.d
        install -m 0644 ${UNPACKDIR}/sshd-devmode.conf ${D}${systemd_system_unitdir}/sshd.socket.d/
    fi
}

FILES:${PN}-sshd += "${systemd_system_unitdir}/sshd.socket.d"
