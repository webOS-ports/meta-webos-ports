# adbd 35.0.2 (meta-luneos-backports-6.1).
#
# Debian's packaging turns key authorisation off on Linux; LuneOS keeps it on:
# every host has to be allowed, by a key in /var/lib/adbd/adb_keys or by the
# user.
#
# 0001: libadbd_auth for LuneOS. adbd asks about a host it does not know on
#       /run/adbd/auth, root's only, with "PK<key> <user@host>", where
#       org.webosports.service.devmode answers "OK" or "NO" once the user
#       has, in the shell. No answer in 75 s, or no service, refuses the
#       host. Hosts allowed "always" are kept in /var/lib/adbd/adb_keys,
#       which devmode writes.
# 0002: USB only; TCP only with ADBD_PORT set in adbd's environment.
# 0003: adbd keeps running as root, as 5.1.1 did (no drop to "adb").
# 0004: a key packet with no payload no longer stops adbd, and one without
#       its NUL is not read past its end (upstream bug, before any host is
#       let in).
#
# 5.1.1's patch 0003 (mincrypt RSA_verify() going to OpenSSL's) has no
# counterpart: adbd 35 checks signatures with BoringSSL itself.
# Named in full: ${BP} is "android-tools-35%" while a bbappend is parsed.
FILESEXTRAPATHS:prepend := "${THISDIR}/android-tools-35.0.2:"
SRC_URI:remove = "file://debian/deb-adbd-adbd-don-t-require-authorization-on-Linux.patch"
SRC_URI += " \
    file://0001-adbd-ask-LuneOS-s-developer-mode-service-about-unkno.patch \
    file://0002-adbd-serve-USB-only-TCP-only-when-ADBD_PORT-asks-for.patch \
    file://0003-adbd-keep-running-as-root.patch \
    file://0004-adb-read-a-host-s-key-no-further-than-its-packet.patch \
"
