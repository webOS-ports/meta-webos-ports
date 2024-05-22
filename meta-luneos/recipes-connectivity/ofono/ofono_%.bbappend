FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV:halium = "3afa0876c6506f76ef2e45d97cb326c5ff9fef4d"
PV:halium = "1.29+git"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "dbus-glib libmce-glib"
RDEPENDS:${PN} += "mobile-broadband-provider-info ofono-conf libsmdpkt-wrapper libqmi libmbim libqrtr-glib"

#For Halium 9 devices we want to use the ofono-binder-plugin, for older devices we might want to use ofono-ril-plugin
RDEPENDS:${PN}:append:halium = " ofono-binder-plugin"

PV = "2.7"
SRC_URI[sha256sum] = "dabf6ef06b94beaad65253200abe3887046a4e722f4fe373c4264f357ae47ad3"

SRC_URI_mainline = " \
  file://0001-common-create-GList-helper-ofono_call_compare.patch \
  file://0002-common-atmodem-move-at_util_call_compare_by_status-t.patch \
  file://0003-common-atmodem-move-at_util_call_compare_by_id-to-dr.patch \
  file://0004-add-call-list-helper-to-manage-voice-call-lists.patch \
  file://0006-Allow-qmi-qrtr-without-data.patch \
  file://msm-modem-uim-selection.sh \
  file://ofono.service \
  file://70-ofono-modem.rules \
"
SRC_URI_mainline:halium = ""
SRC_URI:append = "${SRC_URI_mainline}"

SRC_URI:halium  = " \
  git://github.com/sailfishos/ofono.git;protocol=https;branch=master \
  file://0002-Add-support-for-the-Ericsson-F5521gw-modem.patch;striplevel=2 \
  file://ofono \
  file://ofono-halium.service \
"

S:halium = "${UNPACKDIR}/git/ofono"

# Can't build out of tree right now so we have to build in tree
B:halium = "${S}"

EXTRA_OECONF:append:halium = " --disable-datafiles --disable-sailfish-pushforwarder --enable-extra-modems --disable-rilmodem"

# this version does't support it:
# ERROR: ofono-1.19+gitAUTOINC+b5ed6d16db-r0 do_configure: QA Issue: ofono: configure was passed unrecognised options: --enable-external-ell [unknown-configure-option]
# enabled in oe-core with 1.29 version, however explicitly disabled in SFOS (for reasons unknown to us). Discussions are ongoing about SFOS way forward on oFono
# see https://github.com/sailfishos/ofono/pull/25
EXTRA_OECONF:remove:halium = "--enable-external-ell"

SERVICE_FILE = "ofono.service"
SERVICE_FILE:halium = "ofono-halium.service"

do_install:append() {
    # Override default system service configuration
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/${SERVICE_FILE} ${D}${systemd_unitdir}/system/ofono.service

    if [ -e ${UNPACKDIR}/msm-modem-uim-selection.sh ]; then
        # Install shell script which can help with MSM modems
        install -d ${D}${sbindir}
        install -m 0755 ${UNPACKDIR}/msm-modem-uim-selection.sh ${D}${sbindir}/msm-modem-uim-selection.sh
    fi
    
    if [ -e ${UNPACKDIR}/70-ofono-modem.rules ]; then
        # Install udev rule for mainline modem
        install -d ${D}${sysconfdir}/udev/rules.d
        install -m 0644 ${UNPACKDIR}/70-ofono-modem.rules ${D}${sysconfdir}/udev/rules.d/70-ofono-modem.rules
    fi
}
do_install:append:halium() {
    # Since we use --disable-datafiles we need to install the dbus condif file manually now
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${B}/src/${PN}.conf ${D}${sysconfdir}/dbus-1/system.d/
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE:forcevariable = "enable"

RDEPENDS:${PN}-tests += "python3"
