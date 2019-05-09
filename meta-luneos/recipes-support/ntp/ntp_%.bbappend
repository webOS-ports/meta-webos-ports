# In order to fix the warning message of non-existing ntp-kod file, simply create an empty file.

do_install_append() {
    # Technically we could use ${webos_db8datadir} as well, however since that's specific to webOS db8, prefer to not use it here in case of future changes to webOS db8.
    mkdir -p ${D}/${localstatedir}/db/
    touch ${D}/${localstatedir}/db/ntp-kod
    chmod 666 ${D}/${localstatedir}/db/ntp-kod
}

FILES_${PN} += "${localstatedir}/db/ntp-kod"
