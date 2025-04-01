# Copyright (c) 2018-2025 LG Electronics, Inc.

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

RRECOMMENDS:${PN} += " \
    glibc-gconv-utf-16 \
    glibc-gconv-utf-32 \
"

# Original patch from LuneOS
SRC_URI += " \
    file://0001-Case-insensitive-firmware-name.patch \
"

# Patches coming from webOS OSE
SRC_URI += " \
    file://0001-Fix-advertise-time-out-when-default-is-set-to-zero.patch \
    file://0002-Send-disconnect-signal-on-remote-device-disconnect.patch \
    file://0003-Fetching-device-type-like-BLE-BREDR-from-bluez.patch \
    file://0004-Fix-Gatt-connect-when-device-address-type-is-BDADDR_.patch \
    file://0005-Implementation-to-get-connected-profiles-uuids.patch \
    file://0006-recievePassThrough-commad-support-required-for-webos.patch \
    file://0007-Added-dbus-signal-for-MediaPlayRequest.patch \
    file://0008-avrcp-getting-remote-device-features-list.patch \
    file://0009-Fix-volume-property-not-able-to-set.patch \
    file://0010-Support-enabling-avdtp-delayReport.patch \
    file://0011-Implementation-to-get-connectedUuid-s-in-case-of-inc.patch \
    file://0012-Fix-for-updating-connected-uuids-when-profile-is-dis.patch \
    file://0013-Fix-device-getStatus-not-updated-when-unpaired.patch \
    file://0014-Set-default-pairing-capability-as-NoInputNoOutput.patch \
    file://0015-AVRCP-getting-supported-notification-events.patch \
    file://0016-Modified-MapInstanceName-MapInstanceProperties-parsi.patch \
    file://0017-Enabled-EMAIL-support-based-on-MAPInstance-Name-Modi.patch \
    file://0018-Disabling-DB-Hash-for-Gatt.patch \
    file://0019-Added-Notification-property-in-org.bluez.obex.Messag.patch \
    file://0020-Added-MessageHandle-property-in-org.bluez.obex.Trans.patch \
    file://0021-Create-Message-interface-for-sent-message-related-no.patch \
    file://0022-AVRCP-addToNowPlaying-return-error-when-player-not-s.patch \
    file://0023-AVRCP-MediaItem-object-path-fix.patch \
    file://0024-Revert-a2dp-Add-reverse-discovery.patch \
    file://0025-Add-support-for-meshd-to-use-RAW-channel.patch \
    file://0026-Enable-mesh-fixed-ell-undefined-symbol-error.patch \
    file://0027-Sync-Add-support-for-meshd-to-use-RAW-channel-with-b.patch \
    file://main.conf \
    file://brcm43438.service \
"

inherit webos_systemd
WEBOS_SYSTEMD_SERVICE = "obex.service"

SRC_URI:append:raspberrypi4 = " \
    file://blacklistbtusb.conf \
"

PACKAGECONFIG:append = " mesh \
    sixaxis \
    testing \
"

EXTRA_OECONF:remove = "--enable-external-ell"

do_install:append () {
    install -d ${D}${sysconfdir}/bluetooth
    install -v -m 0644  ${UNPACKDIR}/main.conf ${D}${sysconfdir}/bluetooth/
}

do_install:append:raspberrypi4 () {
    install -d  ${D}${sysconfdir}/modprobe.d
    install -m 644 ${UNPACKDIR}/blacklistbtusb.conf  ${D}${sysconfdir}/modprobe.d/blacklistbtusb.conf
}

FILES:${PN}:append:raspberrypi4 = " ${sysconfdir}/modprobe.d/*"
