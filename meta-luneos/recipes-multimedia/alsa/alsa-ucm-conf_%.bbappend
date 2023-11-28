FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-Add-UCM-configuration-for-Nexus-4-Mako.patch \
    file://0002-Add-UCM-config-file-for-Galaxy-Nexus-maguro.patch \
    file://0003-Add-UCM-files-for-Nexus-7.patch \
    file://0005-Add-UCM-for-PinePhonePro.patch \
    file://0007-Fix-UCM-for-RK817.patch \
"
