# In order to fix the warning message of non-existing ntp-kod file, simply create an empty file.

do_install:append() {
    # Technically we could use ${webos_db8datadir} as well, however since that's specific to webOS db8, prefer to not use it here in case of future changes to webOS db8.
    mkdir -p ${D}/${localstatedir}/db/
    touch ${D}/${localstatedir}/db/ntp-kod
    chmod 666 ${D}/${localstatedir}/db/ntp-kod
}

# sntp reads the KoD file too, and ntpd itself is no longer installed
# (VIRTUAL-RUNTIME_ntp), so ship it with sntp.
FILES:sntp += "${localstatedir}/db/ntp-kod"
