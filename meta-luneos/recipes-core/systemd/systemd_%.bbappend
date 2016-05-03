FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://persistent-storage-rule-mmc-partname.patch \
    file://fd_fdinfo_mnt_id_disablefdinfostat.patch \
"
