# Ship our own sshd_check_keys, restoring the host-key generation fallback that
# oe-core dd27f9d869 removed; the reasoning is in the script. It arrives by
# file:// SRC_URI so it never reaches ${S} and cannot be patched - override it
# instead. Re-diff against oe-core's copy when updating.
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
