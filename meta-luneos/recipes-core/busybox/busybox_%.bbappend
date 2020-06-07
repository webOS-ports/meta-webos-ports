# PmLogDaemon is already a syslog provider, so don't include syslog capability in busybox
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI_remove = "file://syslog.cfg"
SRC_URI += " \
    file://disable-syslog.cfg \
"

