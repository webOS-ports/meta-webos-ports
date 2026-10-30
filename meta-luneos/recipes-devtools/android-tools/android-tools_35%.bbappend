# adbd 35.0.2 (meta-luneos-backports-6.1).
#
# Debian's packaging turns key authorisation off on Linux; LuneOS keeps it on.
# Every host has to be allowed, by a key in /var/lib/adbd/adb_keys or by the
# user. Without the patch auth_required keeps adbd's own default, true.
SRC_URI:remove = "file://debian/deb-adbd-adbd-don-t-require-authorization-on-Linux.patch"
