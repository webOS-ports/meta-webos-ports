# Copyright (c) 2013-2024 LG Electronics, Inc.
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

AUTHOR = "Herb Kuta <herb.kuta@lge.com>"

inherit webos_filesystem_paths

EXTENDPRAUTO:append = "webos16"

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

do_install:prepend() {
    local d
    for d in ${dirs700}; do
        install -v -m 0700 -d ${D}$d
    done
    for d in ${dirs777}; do
        install -v -m 0777 -d ${D}$d
    done
}

do_install:append() {
    # additional entries for fstab
    bbnote "Adding entries to ${sysconfdir}/fstab"

    install -d ${D}/opt
    touch ${D}/opt/.keep

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

PR:append = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'smack1', '', d)}"
do_install[postfuncs] += "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'set_tmpfs_star', '', d)}"

set_tmpfs_star () {
    fstab="${D}/${sysconfdir}/fstab"
    awk '$1 == "tmpfs" {$4=$4",smackfsroot=*"} {print}' $fstab > "$fstab.tmp" && mv $fstab.tmp $fstab
}
