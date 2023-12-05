FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV = "39e2a3f2c57365b78f4f08c0353d6e7abf2709cb"
PV = "1.34+git"

SRCREV:halium = "3afa0876c6506f76ef2e45d97cb326c5ff9fef4d"
PV:halium = "1.29+git"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

DEPENDS += "dbus-glib libmce-glib"
RDEPENDS:${PN} += "mobile-broadband-provider-info ofono-conf"

#For Halium 9 devices we want to use the ofono-binder-plugin, for older devices we might want to use ofono-ril-plugin
RDEPENDS:${PN}:append:halium = " ofono-binder-plugin"


SRC_URI = " \
  git://git.kernel.org/pub/scm/network/ofono/ofono.git;branch=master \
  file://0001-common-create-GList-helper-ofono_call_compare.patch \
  file://0002-common-atmodem-move-at_util_call_compare_by_status-t.patch \
  file://0003-common-atmodem-move-at_util_call_compare_by_id-to-dr.patch \
  file://0004-add-call-list-helper-to-manage-voice-call-lists.patch \
  file://0004-support-smdpkt.patch \
  file://0005-qmimodem-implement-voice-calls.patch \
  file://0001-Fix-build-with-ell-0.39-by-restoring-unlikely-macro-.patch \
  file://ofono \
  file://ofono.service \
"

SRC_URI:halium  = " \
  git://github.com/sailfishos/ofono.git;protocol=https;branch=master \
  file://0002-Add-support-for-the-Ericsson-F5521gw-modem.patch;striplevel=2 \
  file://ofono \
  file://ofono-halium.service \
"

S = "${WORKDIR}/git"
S:halium = "${WORKDIR}/git/ofono"

# Can't build out of tree right now so we have to build in tree
B = "${S}"

EXTRA_OECONF:append = " --disable-datafiles"
EXTRA_OECONF:append:halium = " --disable-sailfish-pushforwarder --enable-extra-modems --disable-rilmodem"

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
    install -m 0644 ${WORKDIR}/${SERVICE_FILE} ${D}${systemd_unitdir}/system/ofono.service

    # Since we use --disable-datafiles we need to install the dbus condif file manually now
    install -d ${D}${sysconfdir}/dbus-1/system.d
    install -m 0644 ${B}/src/${PN}.conf ${D}${sysconfdir}/dbus-1/system.d/
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE:forcevariable = "enable"

RDEPENDS:${PN}-tests += "python3"
