DESCRIPTION = "Specific configuration for GeoClue2 with Google Location Services API key for LuneOS"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

#FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://0001-Use-google-as-default-and-add-api-key.patch"

#S = "${WORKDIR}/geoclue-${PV}"
#B = "${S}"
