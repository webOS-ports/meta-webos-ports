SUMMARY = "Lightweight USB Media Transfer Protocol responder daemon"
DESCRIPTION = "uMTP-Responder serves files over MTP through a FunctionFS or \
GadgetFS USB gadget function. It replaces the libhybris-based mtp-server: it \
has no Android dependencies, so the same MTP stack runs on Halium and \
mainline devices alike."
HOMEPAGE = "https://github.com/viveris/uMTP-Responder"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "systemd"

# master past umtprd-1.8.1: MTP I/O thread hang fix and path sanitizing
PV = "1.8.1+git"
SRCREV = "153f76d65608c184a972ad7d0b4ae605e56c0972"

SRC_URI = " \
    git://github.com/viveris/uMTP-Responder.git;protocol=https;branch=master \
    file://umtprd.conf \
    file://luneos-mtp-gadget \
    file://umtprd.service \
"

S = "${UNPACKDIR}/${BB_GIT_DEFAULT_DESTSUFFIX}"

inherit systemd

# The Makefile hardcodes -O3 and -s (pre-stripped binaries upset OE's QA), so
# pass the flags it would compose ourselves. -DUSE_SYSLOG routes logging to
# syslog, -DSYSTEMD_NOTIFY makes umtprd signal readiness once the FunctionFS
# endpoints are configured, which umtprd.service relies on to bind the UDC in
# ExecStartPost.
EXTRA_OEMAKE = " \
    CFLAGS='${CFLAGS} -I./inc -Wall -DUSE_SYSLOG -DSYSTEMD_NOTIFY' \
    LDFLAGS='${LDFLAGS} -lpthread -lrt -lsystemd' \
"

do_compile() {
    oe_runmake umtprd
}

do_install() {
    install -D -m 0755 ${S}/umtprd ${D}${bindir}/umtprd
    install -D -m 0755 ${UNPACKDIR}/luneos-mtp-gadget ${D}${bindir}/luneos-mtp-gadget
    install -D -m 0644 ${UNPACKDIR}/umtprd.conf ${D}${sysconfdir}/umtprd/umtprd.conf
    install -D -m 0644 ${UNPACKDIR}/umtprd.service ${D}${systemd_system_unitdir}/umtprd.service
}

SYSTEMD_SERVICE:${PN} = "umtprd.service"
# Started on demand through luna://com.palm.storage (storaged -> nyx msm_mtp),
# not unconditionally at boot.
SYSTEMD_AUTO_ENABLE = "disable"
