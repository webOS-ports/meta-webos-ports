FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# settingsservice serves any key to a caller the hub lets call its *Priv methods,
# and otherwise only the keys listed in settingsservice.allow. No API permission
# file lists the *Priv methods, so that check fails for everyone, and upstream
# ships no allow file - which leaves every system service with "Access denied".
#
# audiod needs sound/soundOutputList and sound/soundInputList to restore the
# master volume at startup, and event-monitor needs localeInfo.
SRC_URI += "file://settingsservice.allow"

# for the added allow file
PR = "r3"

do_install:append() {
    install -d ${D}${webos_sysconfdir}
    install -m 0644 ${UNPACKDIR}/settingsservice.allow ${D}${webos_sysconfdir}/settingsservice.allow
}
