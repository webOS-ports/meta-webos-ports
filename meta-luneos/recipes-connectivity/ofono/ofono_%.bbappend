FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"


DEPENDS += "dbus-glib libmce-glib"
RDEPENDS:${PN} += "mobile-broadband-provider-info ofono-conf"
# Mainline modem stacks. A Halium device drives its modem through binder and
# needs none of this.
RDEPENDS:${PN}:append:mainline = " libsmdpkt-wrapper libqmi libmbim libqrtr-glib"

# Halium machines drive the modem through ofono-binder-plugin, which needs
# the extension APIs. Everything else is identical, so one oFono now serves
# both; ofono-halium is retired.
RDEPENDS:${PN}:append:halium = " ofono-ext ofono-ext-plugin ofono-binder-plugin"

SRC_URI:append = " \
  file://0001-common-create-GList-helper-ofono_call_compare.patch \
  file://0002-common-atmodem-move-at_util_call_compare_by_status-t.patch \
  file://0003-common-atmodem-move-at_util_call_compare_by_id-to-dr.patch \
  file://0004-add-call-list-helper-to-manage-voice-call-lists.patch \
  file://0006-Allow-qmi-qrtr-without-data.patch \
  file://0007-sim-add-org.ofono.EuiccManager-interface.patch \
  file://0008-qmimodem-add-logical-channel-support.patch \
  file://msm-modem-uim-selection.sh \
  file://ofono.service \
  file://ofono-halium.service \
  file://70-ofono-modem.rules \
"

# Series that lets out-of-tree plugins supply atom drivers again, and that
# provides the APIs the Sailfish extension layer is written against.
# oFono 2.x removed runtime driver registration in favour of link-time
# collection, which no dlopen()ed plugin can join.
#
# Each patch stands alone and is shaped to be sent upstream. Keep them
# separate: squashing is what makes a version bump expensive.
SRC_URI:append = " \
  file://0100-build-export-daemon-internals-to-external-plugins.patch \
  file://0101-core-add-a-runtime-atom-driver-registry.patch \
  file://0102-core-restore-the-public-atom-driver-registration-API.patch \
  file://0103-core-make-the-shared-3GPP-enumerations-public.patch \
  file://0104-core-expose-the-atom-accessors-external-drivers-need.patch \
  file://0105-core-answer-the-legacy-provisioning-API-from-the-pro.patch \
  file://0106-core-add-the-public-helper-facade-from-ofono-misc.h.patch \
  file://0107-core-let-plugins-use-oFono-s-storage-directory.patch \
  file://0108-core-add-SIM-iccid-imsi-watches-and-the-netreg-opera.patch \
  file://0109-core-let-callers-build-a-PropertyChanged-signal-with.patch \
  file://0110-core-add-open_channel2-and-the-STK-ready-callback.patch \
  file://0111-core-restore-Sailfish-s-exact-SIM-driver-version-bou.patch \
  file://0112-core-make-the-public-headers-self-contained.patch \
  file://0113-build-make-the-installed-headers-usable-from-out-of-.patch \
  file://0114-radio-settings-never-report-a-technology-preference-.patch \
  file://0115-ussd-accept-pre-decoded-UTF-8-from-drivers.patch \
"

# On Halium the modem is driven by ofono-binder-plugin. The in-tree ril
# driver also claims /ril_0 and gets there first, so binder registration
# fails with "Modem register failed on path /ril_0". ofono-halium built
# with --disable-rilmodem for the same reason.
# --disable-rilmodem: the in-tree ril driver claims /ril_0 before the binder
# plugin can, and binder registration then fails with -5.
#
# --disable-qmimodem: this builds plugins/qrtrqmi.c, and udevng then creates a
# 'qrtrsoc' embedded modem for the same hardware the vendor RIL owns. Two
# clients on one modem, and qcrild dies. ofono-halium never had qmi support at
# all, which is why this only appeared after the move to upstream oFono.
#
# --disable-mbimmodem: same reasoning, and it drops the libmbim dependency.
EXTRA_OECONF:append:halium = " --disable-rilmodem --disable-qmimodem --disable-mbimmodem"

SERVICE_FILE = "ofono.service"

# Halium gets its own unit. The mainline one runs msm-modem-uim-selection.sh
# as ExecStartPre, which probes for a Qualcomm QMI modem that a binder device
# does not have; it blocks startup for minutes and then reports "No modem
# available". The Halium unit also passes --nobacktrace and picks up
# $OFONO_DEBUG from /etc/ofono/*.conf.
SERVICE_FILE:halium = "ofono-halium.service"

do_install:append() {
    # Override default system service configuration
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/${SERVICE_FILE} ${D}${systemd_unitdir}/system/ofono.service

    if [ -e ${UNPACKDIR}/msm-modem-uim-selection.sh ] && \
       [ "${SERVICE_FILE}" = "ofono.service" ]; then
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

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE:forcevariable = "enable"

RDEPENDS:${PN}-tests += "python3"
