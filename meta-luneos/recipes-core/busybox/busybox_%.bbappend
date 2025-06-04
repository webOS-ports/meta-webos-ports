# PmLogDaemon is already a syslog provider, so don't include syslog capability in busybox
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:remove = "file://syslog.cfg"
SRC_URI += " \
    file://disable-syslog.cfg \
    file://enable-devmem.cfg \
"

