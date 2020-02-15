FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "0db662bd6ba4070838bf143df5ee24c949a8c0df"
PV = "1.31+git${SRCPV}"

SRCREV_halium = "717f6452aad0d15bf9b3af3491fdd86b87d31315"
PV_halium = "1.21+git${SRCPV}"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "libglibutil dbus-glib libgrilio libmce-glib"
RDEPENDS_${PN} += "mobile-broadband-provider-info ofono-conf"

SRC_URI = " \
  git://git.kernel.org/pub/scm/network/ofono/ofono.git \
  file://0001-common-create-GList-helper-ofono_call_compare.patch \
  file://0002-common-atmodem-move-at_util_call_compare_by_status-t.patch \
  file://0003-common-atmodem-move-at_util_call_compare_by_id-to-dr.patch \
  file://0004-add-call-list-helper-to-manage-voice-call-lists.patch \
  file://0005-qmimodem-implement-voice-calls.patch \
  file://ofono \
  file://ofono.service \
"  

SRC_URI_halium  = " \
  git://git.merproject.org/mer-core/ofono.git \
  file://0001-Enable-the-various-modem-plugins-we-support-again.patch;striplevel=2 \
  file://0002-Add-support-for-the-Ericsson-F5521gw-modem.patch;striplevel=2 \
  file://ofono \
  file://ofono-halium.service \
"

S = "${WORKDIR}/git"
S_halium = "${WORKDIR}/git/ofono"

# Can't build out of tree right now so we have to build in tree
B = "${S}"

EXTRA_OECONF_append_halium = " --disable-sailfish-pushforwarder --enable-sailfish-rilmodem"

# this version does't support it:
# ERROR: ofono-1.19+gitAUTOINC+b5ed6d16db-r0 do_configure: QA Issue: ofono: configure was passed unrecognised options: --enable-external-ell [unknown-configure-option]
# enabled in oe-core with 1.29 version
EXTRA_OECONF_remove_halium = "--enable-external-ell"

SERVICE_FILE = "ofono.service"
SERVICE_FILE_halium = "ofono-halium.service"

do_install_append() {
    # Override default system service configuration
    cp -v ${WORKDIR}/${SERVICE_FILE} ${D}${systemd_unitdir}/system/ofono.service
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE_forcevariable = "enable"

RDEPENDS_${PN}-tests += "python3"
