# We're only interested in the libpurple libraries and not the UI or
# any other features so we keep those things disabled here.
PACKAGECONFIG_remove = "consoleui"

EXTRA_OECONF += " \
    --enable-plugins \
    --enable-debug \
"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://ICQ-pw-length.patch \
"