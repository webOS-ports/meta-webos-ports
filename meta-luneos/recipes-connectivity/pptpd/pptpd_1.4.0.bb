SUMMARY = "PoPToP PPTP server, for local VPN testing"
DESCRIPTION = "The poptop PPTP daemon. Built here only so a real PPTP server \
exists on-device for testing the connman pptp VPN plugin against - LuneOS \
does not ship this as a product feature."
HOMEPAGE = "http://poptop.sourceforge.net/"
SECTION = "net"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "https://download.sourceforge.net/project/poptop/pptpd/pptpd-${PV}/pptpd-${PV}.tar.gz \
           file://0001-compat.c-include-string.h-unconditionally.patch \
           file://0002-Makefile.am-do-not-build-the-pppd-logwtmp-plugin.patch \
"
SRC_URI[sha256sum] = "8fcd8b8a42de2af59e9fe8cbaa9f894045c977f4d038bbd6346a8522bb7f06c0"

S = "${UNPACKDIR}/pptpd-${PV}"

DEPENDS = "ppp"

inherit autotools

# pptpd's Makefile.in's "plugins" SUBDIRS handling doesn't cope with an
# out-of-tree build (autotools.bbclass's own B = "${WORKDIR}/build" default
# wins over an S-relative one set before the inherit, since class content
# applies after) - build in-source instead.
B = "${S}"

do_install:append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/samples/pptpd.conf ${D}${sysconfdir}/pptpd.conf
    install -m 0644 ${S}/samples/options.pptpd ${D}${sysconfdir}/ppp/options.pptpd
}

do_install:prepend() {
    install -d ${D}${sysconfdir}/ppp
}

CONFFILES:${PN} = "${sysconfdir}/pptpd.conf ${sysconfdir}/ppp/options.pptpd"

FILES:${PN} += "${sysconfdir}/pptpd.conf ${sysconfdir}/ppp/options.pptpd"

RDEPENDS:${PN} = "ppp"
