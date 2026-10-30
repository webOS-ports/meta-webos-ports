FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# settingsservice serves any key to a caller the hub lets call its *Priv methods,
# and otherwise only the keys listed in settingsservice.allow. Upstream lists
# the *Priv methods in no API permission file, so that check fails for everyone,
# and ships no allow file - which leaves every system service with "Access
# denied". settingsservice's 0003 patch puts getSystemSettingsPriv alone in
# settings.private.query, for a service that needs keys it alone should read
# (WAM); keys any client may read belong in the allow file.
#
# audiod needs sound/soundOutputList and sound/soundInputList to restore the
# master volume at startup, sound/bluetoothHeadsetTakesCall and
# sound/bluetoothAlertsOnSpeaker for Bluetooth Settings' "Headset takes calls"
# and "Alerts on phone speaker", sound/ringtoneVolume and sound/ringtoneMuted
# for the ringtone volume, sound/callVolumeEarpiece, callVolumeSpeaker,
# callVolumeWired and callVolumeBluetooth for the in-call volume of each route,
# and event-monitor needs localeInfo.
SRC_URI += "file://settingsservice.allow"

# for the added allow file
PR = "r6"

do_install:append() {
    install -d ${D}${webos_sysconfdir}
    install -m 0644 ${UNPACKDIR}/settingsservice.allow ${D}${webos_sysconfdir}/settingsservice.allow
}
