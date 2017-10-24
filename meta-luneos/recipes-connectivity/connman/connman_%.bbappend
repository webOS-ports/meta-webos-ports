FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += "file://0001-connman.service.in-start-after-android-system.servic.patch"

PACKAGECONFIG[nfc] = "--enable-neard, --disable-neard, neard, neard"
RRECOMMENDS_${PN} += "neard"
