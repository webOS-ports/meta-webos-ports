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
# 0005: after a USB disconnect adbd waits for its worker thread with
#       pthread_kill(t, 0) == ESRCH, which glibc 2.34+ never returns for an
#       unjoined thread, so adbd hung for good; it now waits for a flag.
# 0006: blocking read()/write() on the FunctionFS endpoints when io_submit()
#       refuses them with EINVAL. FunctionFS has AIO from Linux 3.15 only, so
#       on older kernels without a backport (mako-halium, 3.4.113) every
#       connection failed at its first read and the host saw "offline".
# 0007: with LUNEOS_ADBD_ANY_KEY defined, no authorisation at all. Only
#       adbd-initramfs (below) is built with it.
#
# adbd checks signatures with BoringSSL itself, so 5.1.1's OpenSSL fix for
# mincrypt's RSA_verify() has no counterpart here.
# Named in full: ${BP} is "android-tools-35%" while a bbappend is parsed.
FILESEXTRAPATHS:prepend := "${THISDIR}/android-tools-35.0.2:"
SRC_URI:remove = "file://debian/deb-adbd-adbd-don-t-require-authorization-on-Linux.patch"
SRC_URI += " \
    file://0001-adbd-ask-LuneOS-s-developer-mode-service-about-unkno.patch \
    file://0002-adbd-serve-USB-only-TCP-only-when-ADBD_PORT-asks-for.patch \
    file://0003-adbd-keep-running-as-root.patch \
    file://0004-adb-read-a-host-s-key-no-further-than-its-packet.patch \
    file://0005-adbd-notice-that-the-USB-worker-has-exited-on-glibc.patch \
    file://0006-adbd-fall-back-to-blocking-FunctionFS-I-O-without-AI.patch \
    file://0007-adbd-let-any-host-in-when-built-for-the-initramfs-debug.patch \
"

# adbd starts only while USB debugging and developer mode are both on; see
# the drop-in. adbd runs as root.
SRC_URI += "file://adbd-devmode.conf"

do_install:append:class-target() {
    install -D -m 0644 ${UNPACKDIR}/adbd-devmode.conf \
        ${D}${systemd_system_unitdir}/android-tools-adbd.service.d/05-devmode.conf
    # The keys' directory: root's alone. devmode writes adb_keys in it (0600).
    install -d -m 0700 ${D}${localstatedir}/lib/adbd
}

FILES:${PN}-adbd += " \
    ${systemd_system_unitdir}/android-tools-adbd.service.d/05-devmode.conf \
    ${localstatedir}/lib/adbd \
"

# adbd for the Halium initramfs's debug shell (INITRAMFS_DEBUG_SHELL = "1",
# meta-android/conf/layer.conf), which starts it when the boot fails. The
# rootfs's adbd is no use there: it asks the developer mode service about
# every host, and the initramfs has neither that service nor adb_keys, so it
# would let nobody in. This one lets every host in (patch 0007), so it is
# built only in debug-shell builds and a release feed never has it.
#
# Its own package, installed as /usr/bin/adbd-initramfs, so it can never
# replace the rootfs's adbd. It is built without systemd (the initramfs has
# none) and depends on ${PN}-libs alone, not on ${PN}: ${PN} pulls in 7-Zip
# and android-tools-conf-configfs, about 7 MB the initramfs has no use for
# and which some boot partitions cannot spare.
LUNEOS_ADBD_INITRAMFS = "${@'1' if d.getVar('INITRAMFS_DEBUG_SHELL') == '1' else '0'}"

do_compile:append:class-target() {
    if [ "${LUNEOS_ADBD_INITRAMFS}" = "1" ]; then
        # adbd.mk names its objects and its output after NAME, so this
        # neither reuses nor overwrites the rootfs adbd's objects.
        CXXFLAGS="${CXXFLAGS} -DLUNEOS_ADBD_ANY_KEY" \
            oe_runmake -f ${S}/debian/system/adbd.mk -C ${S} NAME=adbd-initramfs
    fi
}

do_install:append:class-target() {
    if [ "${LUNEOS_ADBD_INITRAMFS}" = "1" ]; then
        install -D -p -m0755 ${S}/debian/out/system/adbd-initramfs \
            ${D}${bindir}/adbd-initramfs
    fi
}

# The Android libraries adbd links against, apart from ${PN}, so that
# adbd-initramfs can have them without the rest. ${PN} still depends on
# them, so the rootfs gets exactly the files it had before.
PACKAGES =+ "${PN}-adbd-initramfs ${PN}-libs"
FILES:${PN}-libs = "${libdir}/android/*"
PRIVATE_LIBS:${PN}-libs = "liblog.so.0 libbase.so.0 libcutils.so.0"
RDEPENDS:${PN}:append:class-target = " ${PN}-libs"
# adbd links them too; the QA check wants the provider named directly.
RDEPENDS:${PN}-adbd += "${PN}-libs"
FILES:${PN}-adbd-initramfs = "${bindir}/adbd-initramfs"
# Its libraries in ${libdir}/android are private, so the automatic
# shared-library dependencies cannot find them.
RDEPENDS:${PN}-adbd-initramfs = "${PN}-libs"
RRECOMMENDS:${PN}-adbd-initramfs = "kernel-module-libcomposite kernel-module-usb-f-fs"
