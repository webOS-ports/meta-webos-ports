# adbd 5.1.1's key authorisation (audit C2), in a bbappend of its own because
# the patches are against 5.1.1's sources (meta-oe's dynamic-layers selinux
# recipe, 29.0.6, is a different adbd).
#
# 0001: every host has to be allowed by the user, as with ro.adb.secure=1.
#       adbd asks on /run/adbd/auth, root's only, where
#       org.webosports.service.devmode answers once the user has, in the
#       shell. Hosts allowed "always" are kept in /var/lib/adbd/adb_keys.
# 0002: USB only; no TCP 5555 when there is no USB gadget.
#
# They go on top of meta-oe's patches, so patch 0011 (no Android properties)
# is in already: adbd no longer asks properties whether to authenticate.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += " \
    file://0001-adb-require-key-authorisation-asked-through-a-LuneOS.patch;patchdir=system/core \
    file://0002-adb-listen-on-USB-only.patch;patchdir=system/core \
"

# The keys' directory: root's alone. devmode writes adb_keys in it (0600).
do_install:append:class-target() {
    install -d -m 0700 ${D}${localstatedir}/lib/adbd
}

FILES:${PN}-adbd += "${localstatedir}/lib/adbd"
