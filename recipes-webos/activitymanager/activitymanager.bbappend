FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
PRINC := "${@int(PRINC) + 1}"

# Patch is submitted upstream and can be remove once upstream releases a new version
# containing this patch.
SRC_URI += " file://correct-mojodb-include.patch"
