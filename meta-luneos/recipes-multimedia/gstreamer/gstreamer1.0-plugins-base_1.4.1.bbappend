FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-streamsynchronizer-don-t-unset-DISCONT-flag.patch \
"
