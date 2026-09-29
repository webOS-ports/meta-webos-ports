# Ship our own sshd_check_keys, which restores the host-key generation fallback
# that oe-core dd27f9d869 removed. See the comment in the script itself.
#
# Overriding the file rather than patching it: sshd_check_keys arrives via a
# file:// SRC_URI entry, so it is unpacked into UNPACKDIR and never reaches ${S},
# which means a .patch would have nothing to apply to. FILESEXTRAPATHS:prepend
# puts this directory ahead of oe-core's, so our copy wins.
#
# When updating oe-core, re-diff this against
#     openembedded-core/meta/recipes-connectivity/openssh/openssh/sshd_check_keys
# so upstream fixes are not silently dropped.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
