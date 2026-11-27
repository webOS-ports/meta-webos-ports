FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# oe-core's ofono_2.19.bb sets no PR. Without one here, a rebuilt package
# carries the same version as the installed one and opkg won't upgrade it.
# Bump it whenever the patches below change what is built.
PR = "r7"

DEPENDS += "dbus-glib libmce-glib"
# The modem stack differs per machine class. The default is the mainline
# kernel stack; a Halium device instead drives its modem through
# ofono-binder-plugin, which needs the extension APIs, and must not pull in
# the mainline stack. Everything else is identical, so one oFono now serves
# both; ofono-halium is retired.
OFONO_MODEM_STACK_RDEPENDS = "libsmdpkt-wrapper libqmi libmbim libqrtr-glib"
# dummy-netd answers bionic's getaddrinfo() for the vendor side: without it no
# vendor daemon resolves a name, and on MediaTek epdg_wod never finds its ePDG
# (no VoWiFi). Its unit orders itself before ofono.
OFONO_MODEM_STACK_RDEPENDS:halium = "ofono-ext ofono-ext-plugin ofono-binder-plugin libgbinder-tools dummy-netd"
RDEPENDS:${PN} += "mobile-broadband-provider-info ofono-conf ${OFONO_MODEM_STACK_RDEPENDS}"

SRC_URI:append = " \
  file://0001-common-create-GList-helper-ofono_call_compare.patch \
  file://0002-common-atmodem-move-at_util_call_compare_by_status-t.patch \
  file://0003-common-atmodem-move-at_util_call_compare_by_id-to-dr.patch \
  file://0004-add-call-list-helper-to-manage-voice-call-lists.patch \
  file://0006-Allow-qmi-qrtr-without-data.patch \
  file://0007-sim-add-org.ofono.EuiccManager-interface.patch \
  file://0008-qmimodem-add-logical-channel-support.patch \
  file://0009-gobi-support-embedded-QMUX-modems-on-BAM-DMUX-SoCs.patch \
  file://msm-modem-uim-selection.sh \
  file://ofono-binder-wait.sh \
  file://ofono.service \
  file://ofono-halium.service \
  file://70-ofono-modem.rules \
  file://ofono.service.d/luneos-hardening.conf \
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
  file://0116-sim-do-not-walk-a-freed-watch-list-in-open_channel_cb.patch \
  file://0117-hfp_ag_bluez5-register-the-profile-again-when-BlueZ-r.patch \
  file://0118-unit-test-that-the-HFP-AG-profile-follows-BlueZ.patch \
  file://0119-emulator-let-one-emulator-serve-several-modems.patch \
  file://0120-emulator-keep-the-command-handlers-of-each-modem-apa.patch \
  file://0121-emulator-merge-the-call-indicators-of-all-modems.patch \
  file://0122-voicecall-answer-headset-commands-on-the-modem-that.patch \
  file://0123-hfp_ag_bluez5-serve-every-voice-capable-modem-from-o.patch \
  file://0124-hfp_ag_bluez5-remove-the-HUP-watch-together-with-its.patch \
  file://0125-emulator-accept-AT-BAC-with-CVSD-anywhere-in-the-lis.patch \
  file://0126-unit-test-one-HFP-emulator-serving-the-calls-of-two.patch \
  file://0127-unit-test-that-each-headset-gets-every-voice-capable.patch \
  file://0128-emulator-do-not-write-to-a-headset-whose-link-has-gone.patch \
  file://0129-smsutil-take-cell-broadcast-topics-up-to-9999.patch \
  file://0130-cbs-a-public-warning-is-not-a-base-station-id.patch \
  file://0131-unit-decode-the-pages-of-a-real-NL-Alert.patch \
  file://0132-core-add-a-queue-for-D-Bus-requests.patch \
  file://0133-unit-test-the-D-Bus-request-queue.patch \
  file://0134-network-queue-Scan-and-Register-requests.patch \
  file://0135-radio-settings-queue-requests.patch \
  file://0136-sms-queue-GetProperties-requests-while-waiting-for-s.patch \
  file://0137-call-forwarding-allow-multiple-pending-GetProperties.patch \
  file://0138-unit-test-the-atoms-that-queue-D-Bus-requests.patch \
  file://0139-sim-forget-the-IMPI-when-the-SIM-state-is-freed.patch \
  file://0140-sim-let-an-LPA-reset-the-card-through-EuiccManager.patch \
  file://0141-unit-test-EuiccManager.ResetCard.patch \
  file://0142-sim-report-the-card-slots-when-there-is-no-card.patch \
  file://0143-unit-test-the-card-slots-with-no-card-present.patch \
  file://0144-common-name-the-5G-access-technologies.patch \
  file://0145-plugins-report-SMS-status-reports-on-D-Bus-org.ofono.patch \
  file://0146-unit-test-the-SMS-status-report-signal.patch \
  file://0147-provision-keep-the-best-access-point-of-each-type.patch \
  file://0148-provisiontool-merge-providers-that-share-an-MCC-MNC.patch \
  file://0149-gprs-add-an-XCAP-context-type.patch \
  file://0150-gprs-provision-the-XCAP-access-point-of-a-SIM-that-h.patch \
  file://0151-unit-test-provisioning-an-XCAP-access-point.patch \
  file://0152-voicecall-say-whether-a-call-is-carried-over-IMS.patch \
  file://0153-unit-test-the-IMS-property-of-a-call.patch \
"

# The XCAP access point of each network, which oFono's own provider data
# lacks: the modem needs it for supplementary services on LTE (call
# forwarding, waiting, barring, caller ID), or it makes every such request
# on the circuit-switched network and takes the phone off LTE each time.
# Built from Android's APN lists by xcap-apns.py, which says where from.
SRC_URI:append = " file://xcap-apns.json"

# Add them to data/provision.json before provision.db is built from it.
# Only for networks the database already has, so a network never ends up
# with an XCAP context and no internet one.
python ofono_add_xcap_apns() {
    import json
    import os

    path = os.path.join(d.getVar('S'), 'data', 'provision.json')
    with open(path) as f:
        providers = json.load(f)

    if any('xcap' in apn.get('type', [])
           for p in providers for apn in p['apns']):
        return

    known = {i for p in providers if 'spn' not in p for i in p['ids']}

    with open(os.path.join(d.getVar('UNPACKDIR'), 'xcap-apns.json')) as f:
        xcap = [p for p in json.load(f) if p['ids'][0] in known]

    with open(path, 'w') as f:
        json.dump(providers + xcap, f, indent=2, ensure_ascii=False)
        f.write('\n')

    bb.note('Added the XCAP access points of %d networks' % len(xcap))
}
do_patch[postfuncs] += "ofono_add_xcap_apns"

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

    # Halium only: binder slot readiness probe, invoked from ofono-halium.service.
    if [ "${SERVICE_FILE}" = "ofono-halium.service" ]; then
        install -d ${D}${sbindir}
        install -m 0755 ${UNPACKDIR}/ofono-binder-wait.sh ${D}${sbindir}/ofono-binder-wait.sh
    fi

    # Sandbox for both units; see the file for what ofonod still needs.
    install -D -m 0644 ${UNPACKDIR}/ofono.service.d/luneos-hardening.conf \
        ${D}${systemd_unitdir}/system/ofono.service.d/luneos-hardening.conf

    # The provider database provision.db is built from, as readable JSON:
    # the Settings app lists from it every access point of the SIM's
    # network, where ofono itself keeps only one per type.
    install -m 0644 ${S}/data/provision.json ${D}${datadir}/ofono/provision.json

    if [ -e ${UNPACKDIR}/70-ofono-modem.rules ]; then
        # Install udev rule for mainline modem
        install -d ${D}${sysconfdir}/udev/rules.d
        install -m 0644 ${UNPACKDIR}/70-ofono-modem.rules ${D}${sysconfdir}/udev/rules.d/70-ofono-modem.rules
    fi
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE:forcevariable = "enable"

RDEPENDS:${PN}-tests += "python3"
