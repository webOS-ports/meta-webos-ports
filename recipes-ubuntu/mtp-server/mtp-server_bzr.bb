SUMMARY = "Small server implementation of MTP (based on Android)"
LICENSE = "GPL-3.0 & Apache-2.0"
LIC_FILES_CHKSUM = " \
    file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
    file://MODULE_LICENSE_APACHE2;md5=d41d8cd98f00b204e9800998ecf8427e \
    file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595 \
"

DEPENDS += "boost"

PV = "0.0.1+bzr${SRCPV}"
SRCREV = "35"

SRC_URI = "bzr://bazaar.launchpad.net/~phablet-team/mtp/trunk;protocol=http \
    file://use-media-internal.patch;striplevel=0"
S = "${WORKDIR}/trunk"

inherit cmake
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "mtp-server.service"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/mtp-server.service ${D}${systemd_unitdir}/system/
}
