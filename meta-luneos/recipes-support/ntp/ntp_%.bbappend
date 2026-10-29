# In order to fix the warning message of non-existing ntp-kod file, simply create an empty file.

do_install:append() {
    # Technically we could use ${webos_db8datadir} as well, however since that's specific to webOS db8, prefer to not use it here in case of future changes to webOS db8.
    # sntp only ever runs as root (luna-sysservice spawns it, sntp.service
    # has no User=), so root alone needs to write it. A world-writable KoD
    # file would let any local user make sntp shun a time server.
    install -d -m 0755 ${D}${localstatedir}/db
    install -m 0644 /dev/null ${D}${localstatedir}/db/ntp-kod
}

# sntp reads the KoD file too, and ntpd itself is no longer installed
# (VIRTUAL-RUNTIME_ntp), so ship it with sntp.
FILES:sntp += "${localstatedir}/db/ntp-kod"
