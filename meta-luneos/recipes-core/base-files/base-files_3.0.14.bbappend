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

# The user data directory. webOS shipped it 0777 because it used to be a VFAT
# partition, which does not enforce permissions; on LuneOS it is ext4 (Halium
# bind-mounts $datadir/userdata here) or simply part of /. Its writers are root
# (umtprd, obexd, LunaDownloadMgr, SAM-launched apps) plus the "media" group
# (wam, mediaindexer), so it is 0775 root:media - set at boot by
# luneos-media-internal.conf below, because the group is not known to pseudo
# here. "Other" keeps read access: audiod runs as "audio" and plays .wav files
# that callers name by path, so 0770 would break custom sounds kept there.
dirs775 = " \
    ${webos_mountablestoragedir} \
"

do_install:prepend() {
    local d
    for d in ${dirs700}; do
        install -v -m 0700 -d ${D}$d
    done
    for d in ${dirs775}; do
        install -v -m 0775 -d ${D}$d
    done
}

do_install:append() {
    # additional entries for fstab
    bbnote "Adding entries to ${sysconfdir}/fstab"

    install -d ${D}/opt
    touch ${D}/opt/.keep

    generate_fstab_entries >> ${D}${sysconfdir}/fstab

    # tmpfiles "d" also fixes the mode and owner of an existing directory, so
    # devices that ran a 0777 image, and the Halium bind mount, get it too.
    install -d ${D}${nonarch_libdir}/tmpfiles.d
    for d in ${dirs775}; do
        echo "d $d 0775 root media -"
    done > ${D}${nonarch_libdir}/tmpfiles.d/luneos-media-internal.conf

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
    # db8's scratch space: data only, nothing is executed from it
    echo "tmpfs ${webos_db8datadir}/temp tmpfs size=80M,mode=0700,nosuid,nodev,noexec 0 0"
}

PR:append = "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'smack1', '', d)}"
do_install[postfuncs] += "${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'set_tmpfs_star', '', d)}"

set_tmpfs_star () {
    fstab="${D}/${sysconfdir}/fstab"
    awk '$1 == "tmpfs" {$4=$4",smackfsroot=*"} {print}' $fstab > "$fstab.tmp" && mv $fstab.tmp $fstab
}
