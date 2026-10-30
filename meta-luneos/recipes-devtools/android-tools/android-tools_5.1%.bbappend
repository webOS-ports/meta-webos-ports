# adbd 5.1.1's key authorisation (audit C2), in a bbappend of its own because
# the patches are against 5.1.1's sources. LuneOS builds 35.0.2 now
# (luneos-preferred-versions.inc, android-tools_35%.bbappend); this keeps
# 5.1.1 safe should PREFERRED_VERSION_android-tools be set back to it.
#
# 0001: every host has to be allowed by the user, as with ro.adb.secure=1.
#       adbd asks on /run/adbd/auth, root's only, where
#       org.webosports.service.devmode answers once the user has, in the
#       shell. Hosts allowed "always" are kept in /var/lib/adbd/adb_keys.
# 0002: USB only; no TCP 5555 when there is no USB gadget.
# 0003: saved keys are checked with OpenSSL. adbd.mk builds no mincrypt and
#       links -lcrypto, so mincrypt's RSA_verify() call went to OpenSSL's
#       function of that name and adbd crashed once adb_keys held a key. A
#       damaged adb_keys line is skipped with a line in the journal.
#
# They go on top of meta-oe's patches, so patch 0011 (no Android properties)
# is in already: adbd no longer asks properties whether to authenticate.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += " \
    file://0001-adb-require-key-authorisation-asked-through-a-LuneOS.patch;patchdir=system/core \
    file://0002-adb-listen-on-USB-only.patch;patchdir=system/core \
    file://0003-adb-check-saved-keys-with-OpenSSL-skip-a-damaged-adb_keys-line.patch;patchdir=system/core \
"

# android-tools 5.1.1 is 2015 AOSP source and does not compile with a C23 host
# compiler. Ubuntu 26.04's GCC 15.2 defaults to -std=gnu23, where an empty
# parameter list means "takes no arguments" rather than "unspecified":
#
#   ext4_utils/allocate.h:61: u32 allocate_inode();
#   ext4_utils/contents.c:115: error: too many arguments to function
#                              'allocate_inode'; expected 0, have 1
#
# -native and -nativesdk builds use the host compiler, so they are the ones that
# break; the target build uses OE's own toolchain and is unaffected. The recipe
# already appends to CC for these classes, so do the same rather than fighting
# CFLAGS, which its makefiles handle inconsistently. (35.0.2 sets -std=gnu2x
# itself and needs none of this.)
#
# Not a LuneOS-specific problem - this belongs upstream in meta-oe.
CC:append:class-native = " -std=gnu17"
CC:append:class-nativesdk = " -std=gnu17"
