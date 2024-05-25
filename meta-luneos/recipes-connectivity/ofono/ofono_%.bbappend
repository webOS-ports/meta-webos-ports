FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Only non-Halium MACHINEs should be using the mainline upstream ofono version
COMPATIBLE_MACHINE:halium = "^(?!halium)$"

DEPENDS += "dbus-glib libmce-glib"
RDEPENDS:${PN} += "mobile-broadband-provider-info ofono-conf libsmdpkt-wrapper libqmi libmbim libqrtr-glib"

# Bump to 2.7
PV = "2.7"
SRC_URI[sha256sum] = "dabf6ef06b94beaad65253200abe3887046a4e722f4fe373c4264f357ae47ad3"

SRC_URI:append = " \
  file://0001-common-create-GList-helper-ofono_call_compare.patch \
  file://0002-common-atmodem-move-at_util_call_compare_by_status-t.patch \
  file://0003-common-atmodem-move-at_util_call_compare_by_id-to-dr.patch \
  file://0004-add-call-list-helper-to-manage-voice-call-lists.patch \
  file://0004-support-smdpkt.patch \
  file://0006-Allow-qmi-qrtr-without-data.patch \
  file://msm-modem-uim-selection.sh \
  file://ofono.service \
  file://70-ofono-modem.rules \
"

SERVICE_FILE = "ofono.service"

do_install:append() {
    # Override default system service configuration
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${SERVICE_FILE} ${D}${systemd_unitdir}/system/ofono.service

    if [ -e ${WORKDIR}/msm-modem-uim-selection.sh ]; then
        # Install shell script which can help with MSM modems
        install -d ${D}${sbindir}
        install -m 0755 ${WORKDIR}/msm-modem-uim-selection.sh ${D}${sbindir}/msm-modem-uim-selection.sh
    fi
    
    if [ -e ${WORKDIR}/70-ofono-modem.rules ]; then
        # Install udev rule for mainline modem
        install -d ${D}${sysconfdir}/udev/rules.d
        install -m 0644 ${WORKDIR}/70-ofono-modem.rules ${D}${sysconfdir}/udev/rules.d/70-ofono-modem.rules
    fi
}

# meta-systemd sets this to disable but we as distro want it to be enabled by default
SYSTEMD_AUTO_ENABLE:forcevariable = "enable"

RDEPENDS:${PN}-tests += "python3"
