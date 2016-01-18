FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
    file://0001-ucm-fix-handling-of-config-files-when-file-type-is-n.patch \
    file://0002-Add-UCM-configuration-for-Nexus-4-Mako.patch \
    file://0003-Add-UCM-config-file-for-Galaxy-Nexus-maguro.patch \
    file://0004-Add-UCM-files-for-Nexus-7.patch \
"
