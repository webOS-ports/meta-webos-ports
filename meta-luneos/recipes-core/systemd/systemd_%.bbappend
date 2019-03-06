FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://persistent-storage-rule-mmc-partname.patch \
    file://fd_fdinfo_mnt_id_disablefdinfostat.patch \
    file://Disable-ProtectHome-and-ProtectSystem-for-old-kernel.patch \
"

SRC_URI_append_arm = " \
    file://Reroute-chase_symlinks-to-canonicalize_file_name-for.patch \
"
