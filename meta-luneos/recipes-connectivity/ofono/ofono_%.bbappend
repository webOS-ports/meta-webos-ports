FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "0db662bd6ba4070838bf143df5ee24c949a8c0df"
PV = "1.31+git${SRCPV}"

SRCREV:halium = "2ee5e4c8270d0d2b211bf549c7a4e16cd86ca0b4"
PV:halium = "1.23-19+git${SRCPV}"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "libglibutil dbus-glib libgrilio libmce-glib"
RDEPENDS:${PN} += "mobile-broadband-provider-info ofono-conf"

SRC_URI = " \
  git://git.kernel.org/pub/scm/network/ofono/ofono.git;branch=master \
  file://0001-common-create-GList-helper-ofono_call_compare.patch \
  file://0002-common-atmodem-move-at_util_call_compare_by_status-t.patch \
  file://0003-common-atmodem-move-at_util_call_compare_by_id-to-dr.patch \
  file://0004-add-call-list-helper-to-manage-voice-call-lists.patch \
  file://0005-qmimodem-implement-voice-calls.patch \
  file://ofono \
  file://ofono.service \
"

SRC_URI:halium  = " \
  git://github.com/sailfishos/ofono.git;protocol=https;branch=master \
  file://0001-Enable-the-various-modem-plugins-we-support-again.patch;striplevel=2 \
  file://0002-Add-support-for-the-Ericsson-F5521gw-modem.patch;striplevel=2 \
  file://ofono \
  file://ofono-halium.service \
"

S = "${WORKDIR}/git"
S:halium = "${WORKDIR}/git/ofono"

# Can't build out of tree right now so we have to build in tree
B = "${S}"

EXTRA_OECONF:append:halium = " --disable-sailfish-pushforwarder --enable-sailfish-rilmodem --disable-datafiles"

# this version does't support it:
# ERROR: ofono-1.19+gitAUTOINC+b5ed6d16db-r0 do_configure: QA Issue: ofono: configure was passed unrecognised options: --enable-external-ell [unknown-configure-option]
# enabled in oe-core with 1.29 version
EXTRA_OECONF:remove:halium = "--enable-external-ell"

SERVICE_FILE = "ofono.service"
SERVICE_FILE:halium = "ofono-halium.service"

do_install:append() {
    # Override default system service configuration
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${SERVICE_FILE} ${D}${systemd_unitdir}/system/ofono.service
}

do_install:append:halium() {
    # Since we use --disable-datafiles we need to install the dbus condif file manually now
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${B}/src/${PN}.conf ${D}${sysconfdir}/dbus-1/system.d/
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE:forcevariable = "enable"

RDEPENDS:${PN}-tests += "python3"
