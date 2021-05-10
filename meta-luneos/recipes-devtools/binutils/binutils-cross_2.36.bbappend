# Temporary backport here to fix libhybris
# Submitted to oe-core as well
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://0018-Add-DWARF-5-support-in-gold.patch"
