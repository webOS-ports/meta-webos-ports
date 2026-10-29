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

# Say, rather than leave to the compiled-in defaults, that root logs in with a
# key only and that an empty password is never accepted. Both were commented
# out, so the image features allow-root-login and allow-empty-password
# (oe-core's ssh_allow_root_login and ssh_allow_empty_password) could turn them
# into "yes" on every image; LuneOS now sets those features only with
# LUNEOS_DEBUG_ACCESS = "1" (luneos_image.bbclass), and they still rewrite these
# lines then. Edited here, in the main file, and not in a sshd_config.d drop-in:
# sshd takes the first value it reads and Include comes first, so a drop-in
# would also override the debug build's "yes".
do_install:append() {
    for config in sshd_config sshd_config_readonly; do
        f=${D}${sysconfdir}/ssh/$config
        [ -e "$f" ] || continue
        sed -i -e 's/^[#[:space:]]*PermitRootLogin[[:space:]].*/PermitRootLogin prohibit-password/' \
               -e 's/^[#[:space:]]*PermitEmptyPasswords[[:space:]].*/PermitEmptyPasswords no/' "$f"
        grep -q '^PermitRootLogin prohibit-password$' "$f" || bbfatal "$config: no PermitRootLogin line to set"
        grep -q '^PermitEmptyPasswords no$' "$f" || bbfatal "$config: no PermitEmptyPasswords line to set"
    done
}
