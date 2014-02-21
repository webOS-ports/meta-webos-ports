# Copyright (c) 2013 LG Electronics, Inc.

AUTHOR = "Herb Kuta <herb.kuta@lge.com>"

dirs700 = " \
    ${webos_db8datadir} \
    ${webos_db8datadir}/temp \
    ${webos_db8mediadir} \
"

dirs755 += " \
    ${webos_cryptofsdir} \
    ${webos_picapkgdir} \
    ${webos_preferencesdir} \
"

# webOS expects this directory to be writeable by all (because it's typically
# been mounted on a VFAT partition, which doesn't enforce permissions).
dirs777 = " \
    ${webos_mountablestoragedir} \
"

do_install_prepend() {
    local d
    for d in ${dirs700}; do
        install -v -m 0700 -d ${D}$d
    done
    for d in ${dirs777}; do
        install -v -m 0777 -d ${D}$d
    done
}

do_install_append() {
    # additional entries for fstab
    bbnote "Adding entries to ${sysconfdir}/fstab"
    generate_fstab_entries >> ${D}${sysconfdir}/fstab

    bbnote "Ensuring that fstab has exactly one record per mount-point"
    local collisions
    collisions=$(awk '
        { gsub(/\s*(#.*)?$/,"") }
        /^$/ { next }
        ++t[$2] == 2 { printf "%s ", $2 }
        ' ${D}${sysconfdir}/fstab)

    [ -z "$collisions" ] \
        || bbfatal "Found records in fstab with identical mount-points: $collisions"
}

generate_fstab_entries() {
    echo "# additional in-memory storage for db8"
    echo "tmpfs ${webos_db8datadir}/temp tmpfs size=80M,mode=0700 0 0"
}
